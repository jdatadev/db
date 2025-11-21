package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableLongLargeArrayAllocator

        extends MutableLongLargeArrayAllocator<ICachedMutableLongLargeArray, CachedMutableLongLargeArray>
        implements ICachedMutableLongLargeArrayAllocator {

    @Override
    protected CachedMutableLongLargeArray allocateMutableInstance(LongFunction<long[]> createElements, long minimumCapacity) {

        Objects.requireNonNull(createElements);
        Checks.isIntMinimumCapacity(minimumCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.CACHING_ALLOCATOR, minimumCapacity, CachedMutableLongLargeArray::new);
    }
}
