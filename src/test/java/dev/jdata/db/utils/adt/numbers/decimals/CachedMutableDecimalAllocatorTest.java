package dev.jdata.db.utils.adt.numbers.decimals;

import dev.jdata.db.utils.allocators.BaseArrayMinimumCapacityAllocatorTest;

public final class CachedMutableDecimalAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<ICachedMutableDecimal, CachedMutableDecimalAllocator> {

    @Override
    protected ICachedMutableDecimal allocate(CachedMutableDecimalAllocator allocator, int minimumCapacity) {

        return allocator.allocateDecimal(minimumCapacity, 0);
    }

    @Override
    protected CachedMutableDecimalAllocator createAllocator() {

        return new CachedMutableDecimalAllocator();
    }

    @Override
    protected ICachedMutableDecimal[] allocateArray(int length) {

        return new ICachedMutableDecimal[length];
    }

    @Override
    protected void free(CachedMutableDecimalAllocator allocator, ICachedMutableDecimal instance) {

        allocator.freeDecimal(instance);
    }
}
