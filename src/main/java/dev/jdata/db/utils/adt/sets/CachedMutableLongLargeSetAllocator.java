package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableLongLargeSetAllocator

        extends BaseLongCapacityMutableSetAllocator<ICachedMutableLongLargeSet, CachedMutableLongLargeMaxDistanceNonBucketSet>
        implements ICachedMutableLongLargeSetAllocator  {

    @Override
    protected CachedMutableLongLargeMaxDistanceNonBucketSet allocateMutableSet(long minimumCapacity) {

        Checks.isLongMinimumCapacity(minimumCapacity);

        return CachedMutableLongLargeMaxDistanceNonBucketSet.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }
}
