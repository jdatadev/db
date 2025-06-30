package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.CachedMutableIndexList.CacheMutableIndexListAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.ICacheable;

public final class CachedIndexList<T> extends IndexList<T> implements ICachedIndexList<T> {

    private static final CachedIndexList<?> emptyList = new CachedIndexList<>(AllocationType.HEAP_CONSTANT);

    public static final class CacheIndexListAllocator<T> extends IndexListAllocator<T, CachedIndexList<T>, CachedIndexListBuilder<T>, CachedMutableIndexList<T>> implements IAllocators {

        private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

        private final ArrayIndexListBuilderAllocator<T, CachedIndexListBuilder<T>> listBuilderArrayAllocator;
        private final IndexListArrayAllocator<T, CachedIndexList<T>> listArrayAllocator;
        private final CacheMutableIndexListAllocator<T> mutableListAllocator;

        public CacheIndexListAllocator(IntFunction<T[]> createElementsArray) {

            Objects.requireNonNull(createElementsArray);

            this.listBuilderArrayAllocator = new ArrayIndexListBuilderAllocator<>(ALLOCATION_TYPE, c -> new CachedIndexListBuilder<>(ALLOCATION_TYPE, c, this));
            this.listArrayAllocator = new IndexListArrayAllocator<>(ALLOCATION_TYPE, c -> new CachedIndexList<T>(ALLOCATION_TYPE, createElementsArray, c));
            this.mutableListAllocator = new CacheMutableIndexListAllocator<>(createElementsArray);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addInstanceAllocator("listBuilderArrayAllocator", RefType.INSTANTIATED, CachedIndexListBuilder.class, listBuilderArrayAllocator);
            statisticsGatherer.addInstanceAllocator("listArrayAllocator", RefType.INSTANTIATED, CachedIndexList.class, listArrayAllocator);
            statisticsGatherer.addInstanceAllocator("mutableListArrayAllocator", RefType.INSTANTIATED, MutableIndexList.class, mutableListAllocator);
        }

        @Override
        CachedIndexListBuilder<T> allocateIndexListBuilder(int minimumCapacity) {

            return listBuilderArrayAllocator.allocateIndexListBuilder(minimumCapacity);
        }

        @Override
        public void freeIndexListBuilder(CachedIndexListBuilder<T> builder) {

            listBuilderArrayAllocator.freeIndexListBuilder(builder);
        }

        @Override
        public void freeIndexList(CachedIndexList<T> list) {

            listArrayAllocator.freeIndexList(list);
        }

        @Override
        CachedMutableIndexList<T> allocateMutableIndexList(int minimumCapacity) {

            return mutableListAllocator.allocateMutableIndexList(minimumCapacity);
        }

        @Override
        public void freeIndexMutableList(CachedMutableIndexList<T> list) {

            mutableListAllocator.freeMutableIndexList(list);
        }

        @Override
        CachedIndexList<T> allocateIndexListFrom(T[] values, int numElements) {

            final CachedIndexList<T> result = listArrayAllocator.allocateIndexList(numElements);

            result.initialize(values, numElements);

            return result;
        }

        @Override
        CachedIndexList<T> copyToImmutable(MutableIndexList<T> mutableIndexList) {

            return mutableIndexList.makeFromElements(this, (e, n, i) -> i.allocateIndexListFrom(e, n));
        }

        @SuppressWarnings("unchecked")
        @Override
        CachedIndexList<T> emptyList() {

            return (CachedIndexList<T>)emptyList;
        }
    }

    public static final class CachedIndexListBuilder<T> extends IndexListBuilder<T, CachedIndexList<T>, CachedIndexListBuilder<T>> implements ICacheable {

        private final CacheIndexListAllocator<T> listAllocator;

        private CachedIndexListBuilder(AllocationType allocationType, int initialCapacity, CacheIndexListAllocator<T> listAllocator) {
            super(allocationType, initialCapacity, listAllocator);

            this.listAllocator = Objects.requireNonNull(listAllocator);
        }

        @Override
        public final CachedIndexList<T> build() {

            return listAllocator.copyToImmutable(getList());
        }
    }

    private CachedIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private CachedIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    @Override
    public HeapIndexList<T> toHeapAllocated() {

        return new HeapIndexList<>(this);
    }
}
