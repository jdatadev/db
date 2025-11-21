package dev.jdata.db.utils.adt.sets;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableIntSetAllocator extends MutableIntSetAllocator<ICachedMutableIntSet, CachedMutableIntMaxDistanceNonBucketSet> {

    @Override
    protected CachedMutableIntMaxDistanceNonBucketSet allocateMutableInstance(IntFunction<int[]> createElements, int minimumCapacity) {

        Objects.requireNonNull(createElements);
        Checks.isIntMinimumCapacity(minimumCapacity);

        return CachedMutableIntMaxDistanceNonBucketSet.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }
}
