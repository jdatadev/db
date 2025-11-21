package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableIntSetAllocator extends MutableIntSetAllocator<ICachedMutableIntSet, CachedMutableIntMaxDistanceNonBucketSet> {

    @Override
    protected CachedMutableIntMaxDistanceNonBucketSet allocateMutableInstance(IntFunction<int[]> createElements, int minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return CachedMutableIntMaxDistanceNonBucketSet.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }

    @Override
    public ICachedMutableIntSet copyToMutable(IIntIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        return CachedMutableIntMaxDistanceNonBucketSet.copyToMutable(AllocationType.CACHING_ALLOCATOR, mutableFrom);
    }
}
