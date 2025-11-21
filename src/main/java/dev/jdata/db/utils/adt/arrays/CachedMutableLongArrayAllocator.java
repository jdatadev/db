package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableLongArrayAllocator extends MutableLongArrayAllocator<ICachedMutableLongArray, CachedMutableLongArray> implements ICachedMutableLongArrayAllocator {

    @Override
    protected CachedMutableLongArray allocateMutableInstance(IntFunction<long[]> createElements, int minimumCapacity) {

        Objects.requireNonNull(createElements);
        Checks.isIntMinimumCapacity(minimumCapacity);

        return new CachedMutableLongArray(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }
}
