package dev.jdata.db.utils.adt.lists;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.adt.elements.IIterableElements;
import dev.jdata.db.utils.allocators.ADTInstanceAllocator;
import dev.jdata.db.utils.allocators.BaseArrayAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IBuilder;
import dev.jdata.db.utils.allocators.IInstanceAllocator;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class IndexList<T> extends BaseIndexList<T> implements ICapacity, IIndexList<T> {

    public static abstract class IndexListAllocator<T> extends ADTInstanceAllocator<T, Builder<T>> implements IInstanceAllocator<T> {

        @SafeVarargs
        public final IIndexList<T> of(T ... instances) {

            final Builder<T> builder = allocateIndexListBuilder(instances.length);

            builder.addTail(instances);

            return builder.build();
        }

        abstract Builder<T> allocateIndexListBuilder(int minimumCapacity);
        public abstract void freeIndexListBuilder(Builder<T> builder);

        abstract IndexList<T> allocateIndexList(int minimumCapacity);
        public abstract void freeIndexList(IIndexList<T> list);

        abstract MutableIndexList<T> allocateMutableIndexList(int minimumCapacity);
        public abstract void freeIndexMutableList(MutableIndexList<T> list);

        abstract IndexList<T> swapToImmutable(MutableIndexList<T> mutableIndexList);
    }

    public static final class HeapIndexListAllocator<T> extends IndexListAllocator<T> {

        private final IntFunction<T[]> createElementsArray;

        public HeapIndexListAllocator(IntFunction<T[]> createElementsArray) {

            this.createElementsArray = Objects.requireNonNull(createElementsArray);
        }

        @Override
        Builder<T> allocateIndexListBuilder(int minimumCapacity) {

            addAllocatedBuilder(false);

            return new Builder<>(minimumCapacity, this);
        }

        @Override
        public void freeIndexListBuilder(Builder<T> builder) {

            Objects.requireNonNull(builder);

            addFreedBuilder(false);
        }

        @Override
        IndexList<T> allocateIndexList(int minimumCapacity) {

            addAllocatedInstance(false);

            return new IndexList<>(createElementsArray, minimumCapacity);
        }

        @Override
        public void freeIndexList(IIndexList<T> list) {

            Objects.requireNonNull(list);

            addFreedInstance(false);
        }

        @Override
        MutableIndexList<T> allocateMutableIndexList(int minimumCapacity) {

            addAllocatedInstance(false);

            return new MutableIndexList<>(createElementsArray, minimumCapacity);
        }

        @Override
        public void freeIndexMutableList(MutableIndexList<T> list) {

            Objects.requireNonNull(list);

            addFreedInstance(false);
        }

        @Override
        IndexList<T> swapToImmutable(MutableIndexList<T> mutableIndexList) {

            Objects.requireNonNull(mutableIndexList);

            return mutableIndexList.swapToImmutable();
        }
    }

    private static final class ArrayIndexListBuilderAllocator<T> extends BaseArrayAllocator<Builder<T>> {

        ArrayIndexListBuilderAllocator(IntFunction<T[]> createListElementsArray, IndexListAllocator<T> listAllocator) {
            super(c -> new Builder<>(c, listAllocator), l -> Integers.checkUnsignedLongToUnsignedInt(l.getNumElements()));
        }

        Builder<T> allocateIndexListBuilder(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeIndexListBuilder(Builder<T> builder) {

            freeArrayInstance(builder);
        }
    }

    private static final class IndexListArrayAllocator<T> extends BaseArrayAllocator<IndexList<T>> {

        IndexListArrayAllocator(IntFunction<T[]> createListElementsArray) {
            super(c -> new IndexList<>(createListElementsArray, c), l -> Integers.checkUnsignedLongToUnsignedInt(l.getNumElements()));
        }

        IndexList<T> allocateIndexList(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeIndexList(IndexList<T> list) {

            freeArrayInstance(list);
        }
    }

    private static final class MutableIndexListArrayAllocator<T> extends BaseArrayAllocator<MutableIndexList<T>> {

        MutableIndexListArrayAllocator(IntFunction<T[]> createListElementsArray) {
            super(c -> new MutableIndexList<>(createListElementsArray, c), l -> Integers.checkUnsignedLongToUnsignedInt(l.getNumElements()));
        }

        MutableIndexList<T> allocateMutableIndexList(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeMutableIndexList(MutableIndexList<T> list) {

            freeArrayInstance(list);
        }
    }

    public static final class CacheIndexListAllocator<T> extends IndexListAllocator<T> implements IAllocators {

        private final ArrayIndexListBuilderAllocator<T> listBuilderArrayAllocator;
        private final IndexListArrayAllocator<T> listArrayAllocator;
        private final MutableIndexListArrayAllocator<T> mutableListArrayAllocator;

        public CacheIndexListAllocator(IntFunction<T[]> createElementsArray) {

            Objects.requireNonNull(createElementsArray);

            this.listBuilderArrayAllocator = new ArrayIndexListBuilderAllocator<>(createElementsArray, this);
            this.listArrayAllocator = new IndexListArrayAllocator<>(createElementsArray);
            this.mutableListArrayAllocator = new MutableIndexListArrayAllocator<>(createElementsArray);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addInstanceAllocator("listBuilderArrayAllocator", RefType.INSTANTIATED, IndexList.Builder.class, listBuilderArrayAllocator);
            statisticsGatherer.addInstanceAllocator("listArrayAllocator", RefType.INSTANTIATED, IndexList.class, listArrayAllocator);
            statisticsGatherer.addInstanceAllocator("mutableListArrayAllocator", RefType.INSTANTIATED, MutableIndexList.class, mutableListArrayAllocator);
        }

        @Override
        Builder<T> allocateIndexListBuilder(int minimumCapacity) {

            return listBuilderArrayAllocator.allocateIndexListBuilder(minimumCapacity);
        }

        @Override
        public void freeIndexListBuilder(Builder<T> builder) {

            listBuilderArrayAllocator.freeIndexListBuilder(builder);
        }

        @Override
        IndexList<T> allocateIndexList(int minimumCapacity) {

            return listArrayAllocator.allocateIndexList(minimumCapacity);
        }

        @Override
        public void freeIndexList(IIndexList<T> list) {

            listArrayAllocator.freeIndexList((IndexList<T>)list);
        }

        @Override
        MutableIndexList<T> allocateMutableIndexList(int minimumCapacity) {

            return mutableListArrayAllocator.allocateMutableIndexList(minimumCapacity);
        }

        @Override
        public void freeIndexMutableList(MutableIndexList<T> list) {

            mutableListArrayAllocator.freeMutableIndexList(list);
        }

        @Override
        IndexList<T> swapToImmutable(MutableIndexList<T> mutableIndexList) {

            Objects.requireNonNull(mutableIndexList);

            return mutableIndexList.swapToImmutableAndClear();
        }
    }

    @SafeVarargs
    public static <T> IIndexList<T> of(T ... instances) {

        Objects.requireNonNull(instances);

        return new IndexList<>(instances);
    }

    public static <T> IIndexList<T> sortedOf(IIterableElements<T> elements, Comparator<? super T> comparator, IntFunction<T[]> createArray, IndexListAllocator<T> allocator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(createArray);

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(elements.getNumElements());

        final MutableIndexList<T> sorted = allocator != null
                ? allocator.allocateMutableIndexList(numElements)
                : new MutableIndexList<>(createArray, numElements);

        sorted.addTail(elements);

        sorted.sort(comparator);

        return allocator != null
                ? allocator.swapToImmutable(sorted)
                : sorted.swapToImmutable();
    }

    public static <T> Builder<T> createBuilder(IntFunction<T[]> createArray) {

        return createBuilder(createArray, DEFAULT_INITIAL_CAPACITY);
    }

    public static <T> Builder<T> createBuilder(IntFunction<T[]> createArray, int initialCapacity) {

        return new Builder<>(new IndexList<>(createArray, initialCapacity));
    }

    public static <T> Builder<T> createBuilder(IndexListAllocator<T> listAllcator) {

        return createBuilder(DEFAULT_INITIAL_CAPACITY, listAllcator);
    }

    public static <T> Builder<T> createBuilder(int minimumCapacity, IndexListAllocator<T> listAllocator) {

        return listAllocator.allocateIndexListBuilder(minimumCapacity);
    }

    public static final class Builder<T> implements IIndexListMutators<T>, IBuilder<T, Builder<T>> {

        private IndexList<T> list;

        private Builder(IndexList<T> list) {

            this.list = list;
        }

        private Builder(int minimumCapacity, IndexListAllocator<T> listAllocator) {

            Checks.isInitialCapacity(minimumCapacity);
            Objects.requireNonNull(listAllocator);

            this.list = listAllocator.allocateIndexList(minimumCapacity);
        }

        @Override
        public void addHead(T instance) {

            list.addHead(instance);
        }

        @Override
        public void addTail(T instance) {

            list.addTail(instance);
        }

        @Override
        public void addTail(@SuppressWarnings("unchecked") T... instances) {

            list.addTail(instances);
        }

        public IIndexList<T> build() {

            final IndexList<T> result = list;

            if (result == null) {

                throw new IllegalStateException();
            }

            this.list = null;

            return result;
        }

        final long getNumElements() {

            return list.getNumElements();
        }
    }

    private static final IIndexList<?> emptyList = new IndexList<Object>();

    @SuppressWarnings("unchecked")
    public static <T> IIndexList<T> empty() {

        return (IIndexList<T>)emptyList;
    }

    public static <T> IndexList<T> of(T instance) {

        Objects.requireNonNull(instance);

        @SuppressWarnings("unchecked")
        final IntFunction<T[]> createArray = l -> (T[])Array.newInstance(instance.getClass(), l);

        final IndexList<T> result = new IndexList<>(createArray);

        result.addHead(instance);

        return result;
    }

    private IndexList() {

    }

    IndexList(IntFunction<T[]> createArray) {
        this(createArray, DEFAULT_INITIAL_CAPACITY);
    }

    IndexList(IntFunction<T[]> createArray, T[] instances, int numElements) {
        super(createArray, instances, numElements);
    }

    private IndexList(T[] instances) {
        super(instances);
    }

    public IndexList(IntFunction<T[]> createArray, int initialCapacity) {
        super(createArray, initialCapacity);
    }

    @Override
    public final IMutableIndexList<T> copyToMutable() {

        return new MutableIndexList<>(getCreateArray(), this);
    }
}
