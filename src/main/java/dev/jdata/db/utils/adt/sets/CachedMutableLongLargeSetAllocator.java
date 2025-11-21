package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableLongLargeSetAllocator

        extends BaseLongCapacityMutableSetAllocator<ICachedMutableLongLargeSet, CachedMutableLongMaxDistanceNonBucketLargeSet, ILongIterableElementsView>
        implements ICachedMutableLongLargeSetAllocator  {

    @Override
    protected CachedMutableLongMaxDistanceNonBucketLargeSet allocateMutableLargeSet(long minimumCapacity) {

        return CachedMutableLongMaxDistanceNonBucketLargeSet.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }
}
