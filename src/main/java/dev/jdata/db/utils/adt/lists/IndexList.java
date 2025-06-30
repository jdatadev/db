package dev.jdata.db.utils.adt.lists;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IObjectIterableElements;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListBuilder;
import dev.jdata.db.utils.allocators.BaseAllocatableArrayAllocator;
import dev.jdata.db.utils.allocators.BaseArrayAllocator;
import dev.jdata.db.utils.allocators.BuilderInstanceAllocator;
import dev.jdata.db.utils.allocators.IObjectAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.builders.IObjectBuilder;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class IndexList<T> extends BaseIndexList<T> implements IIndexList<T> {

    public static abstract class IndexListAllocator<T, U extends IndexList<T>, V extends IndexListBuilder<T, U, V>, W extends MutableIndexList<T>>

            extends BuilderInstanceAllocator<T, V>
            implements IObjectAllocator<T> {

        abstract V allocateIndexListBuilder(int minimumCapacity);
        public abstract void freeIndexListBuilder(V builder);

        abstract U allocateIndexListFrom(T[] values, int numElements);
        public abstract void freeIndexList(U list);

        abstract W allocateMutableIndexList(int minimumCapacity);
        abstract void freeIndexMutableList(W list);

        abstract U copyToImmutable(MutableIndexList<T> mutableIndexList);
//        abstract U copyToMutable(MutableIndexList<T> mutableIndexList);

        abstract U emptyList();
    }

    static final class ArrayIndexListBuilderAllocator<T, U extends IndexListBuilder<T, ?, U>> extends BaseAllocatableArrayAllocator<U> {

        ArrayIndexListBuilderAllocator(AllocationType allocationType, IntFunction<U> createBuilder) {
            super(createBuilder, l -> Integers.checkUnsignedLongToUnsignedInt(l.getCapacity()));
        }

        U allocateIndexListBuilder(int minimumCapacity) {

            return allocateAllocatableArrayInstance(minimumCapacity);
        }

        void freeIndexListBuilder(U builder) {

            freeAllocatableArrayInstance(builder);
        }
    }

    static final class IndexListArrayAllocator<T, U extends IndexList<T>> extends BaseArrayAllocator<U> {

        IndexListArrayAllocator(AllocationType allocationType, IntFunction<U> createList) {
            super(createList, l -> Integers.checkUnsignedLongToUnsignedInt(l.getNumElements()));
        }

        U allocateIndexList(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeIndexList(U list) {

            freeArrayInstance(list);
        }
    }

    @SafeVarargs
    public static <T> HeapIndexList<T> of(T ... instances) {

        Objects.requireNonNull(instances);

        return new HeapIndexList<>(AllocationType.HEAP, Array.copyOf(instances));
    }

    public static <T> HeapIndexList<T> sortedOf(IObjectIterableElements<T> elements, Comparator<? super T> comparator, IntFunction<T[]> createElementsArray) {

        final HeapIndexList<T> result;

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(elements.getNumElements());

        if (numElements != 0) {

            final MutableIndexList<T> sorted = HeapMutableIndexList.from(createElementsArray, numElements);

            sorted.addTail(elements);

            sorted.sort(comparator);

            result = fromMutableIndexList(AllocationType.HEAP, sorted);
        }
        else {
            result = IndexList.empty();
        }

        return result;
    }

    public static <T, U extends IndexList<T>> U sortedOf(IObjectIterableElements<T> elements, Comparator<? super T> comparator, IndexListAllocator<T, U, ?, ?> allocator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(allocator);

        final U result;

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(elements.getNumElements());

        if (numElements != 0) {

            final MutableIndexList<T> sorted = allocator.allocateMutableIndexList(numElements);

            sorted.addTail(elements);

            sorted.sort(comparator);

            result = allocator.copyToImmutable(sorted);
        }
        else {
            result = allocator.emptyList();
        }

        return result;
    }

    public static <T> HeapIndexListBuilder<T> createBuilder(IntFunction<T[]> createElementsArray) {

        return createBuilder(createElementsArray, DEFAULT_INITIAL_CAPACITY);
    }

    public static <T> HeapIndexListBuilder<T> createBuilder(IntFunction<T[]> createElementsArray, int initialCapacity) {

        return new HeapIndexListBuilder<>(AllocationType.HEAP, createElementsArray, initialCapacity);
    }

    public static <T, U extends IndexListAllocator<T, ?, V, ?>, V extends IndexListBuilder<T, ?, V>> V createBuilder(U listAllcator) {

        return createBuilder(DEFAULT_INITIAL_CAPACITY, listAllcator);
    }

    public static <T, U extends IndexListAllocator<T, ?, V, ?>, V extends IndexListBuilder<T, ?, V>> V createBuilder(int minimumCapacity, U listAllocator) {

        return listAllocator.allocateIndexListBuilder(minimumCapacity);
    }

    public static abstract class IndexListBuilder<T, U extends IndexList<T>, V extends IndexListBuilder<T, U, V>>

            extends ObjectCacheNode
            implements IIndexListBuildable<T, U, V>, IObjectBuilder<T, V> {

        private final MutableIndexList<T> list;

        IndexListBuilder(AllocationType allocationType, int minimumCapacity, IndexListAllocator<T, ?, ?, ? extends MutableIndexList<T>> listAllocator) {
            super(allocationType);

            Checks.isInitialCapacity(minimumCapacity);
            Objects.requireNonNull(listAllocator);

            this.list = listAllocator.allocateMutableIndexList(minimumCapacity);
        }

        @Override
        public final boolean isEmpty() {

            return list.isEmpty();
        }

        @Override
        public final V addTail(T instance) {

            checkIsAllocated();

            list.addTailElement(instance);

            return getThis();
        }

        @Override
        public final V addTail(@SuppressWarnings("unchecked") T ... instances) {

            checkIsAllocated();

            list.addTailElements(instances);

            return getThis();
        }

        final MutableIndexList<T> getList() {
            return list;
        }

        final long getCapacity() {

            return list.getCapacity();
        }

        @SuppressWarnings("unchecked")
        private V getThis() {

            return (V)this;
        }
    }

    private static final HeapIndexList<?> emptyList = new HeapIndexList<>(AllocationType.HEAP);

    @SuppressWarnings("unchecked")
    public static <T> HeapIndexList<T> empty() {

        return (HeapIndexList<T>)emptyList;
    }

    public static <T> HeapIndexList<T> of(T instance) {

        Objects.requireNonNull(instance);

        @SuppressWarnings("unchecked")
        final IntFunction<T[]> createElementsArray = l -> (T[])java.lang.reflect.Array.newInstance(instance.getClass(), l);

        return new HeapIndexList<>(AllocationType.HEAP, createElementsArray, instance);
    }

    public abstract HeapIndexList<T> toHeapAllocated();

    IndexList(AllocationType allocationType) {
        super(allocationType);
    }

    IndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    IndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, instance);
    }

    IndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    IndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    IndexList(IndexList<T> toCopy) {
        super(toCopy);
    }

    static <T> HeapIndexList<T> fromMutableIndexList(AllocationType allocationType, MutableIndexList<T> mutableIndexList) {

        AllocationType.checkIsHeap(allocationType);

        return new HeapIndexList<>(allocationType, mutableIndexList.makeArrayCopy());
    }

    @Override
    public <U extends MutableIndexList<T>> U copyToMutable(IndexListAllocator<T, ? extends IndexList<T>, ?, U> indexListAllocator) {

        Objects.requireNonNull(indexListAllocator);

        final U result = indexListAllocator.allocateMutableIndexList(getIntNumElements());

        result.addTail((BaseObjectArrayList<T>)this);

        return result;
    }
}
