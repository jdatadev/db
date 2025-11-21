package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.CachedIndexList.IndexListArrayAllocator;
import dev.jdata.db.utils.adt.lists.CachedMutableIndexList.CacheMutableIndexListAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

final class CacheIndexListAllocator<T>

        extends IndexListAllocator<T, CachedIndexList<T>, CachedIndexListBuilder<T>, CachedMutableIndexList<T>, BUILDER, BUILDER_ALLOCATOR>
        implements IAllocators {

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
        statisticsGatherer.addInstanceAllocator("mutableListArrayAllocator", RefType.INSTANTIATED, MutableObjectIndexList.class, mutableListAllocator);
    }

    @Override
    CachedIndexListBuilder<T> allocateBuilder(int minimumCapacity) {

        return listBuilderArrayAllocator.allocateIndexListBuilder(minimumCapacity);
    }

    @Override
    public void freeIndexListBuilder(CachedIndexListBuilder<T> builder) {

        listBuilderArrayAllocator.freeIndexListBuilder(builder);
    }

    @Override
    public void freeImmutable(CachedIndexList<T> list) {

        listArrayAllocator.freeIndexList(list);
    }

    @Override
    CachedMutableIndexList<T> allocateMutable(int minimumCapacity) {

        return mutableListAllocator.allocateMutableIndexList(minimumCapacity);
    }

    @Override
    public void freeMutable(CachedMutableIndexList<T> list) {

        mutableListAllocator.freeMutableIndexList(list);
    }

    @Override
    CachedIndexList<T> allocateImmutableFrom(T[] values, int numElements) {

        final CachedIndexList<T> result = listArrayAllocator.allocateIndexList(numElements);

        result.initialize(values, numElements);

        return result;
    }

    @Override
    CachedIndexList<T> copyToImmutable(MutableObjectIndexList<T> mutableIndexList) {

        return mutableIndexList.makeFromElementsAndRecreate(this, (c, e, n, i) -> i.allocateImmutableFrom(e, n));
    }

    @SuppressWarnings("unchecked")
    @Override
    CachedIndexList<T> emptyImmutable() {

        return (CachedIndexList<T>)CachedIndexList.emptyList;
    }
}
