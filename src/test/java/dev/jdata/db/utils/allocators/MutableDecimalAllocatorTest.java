package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.decimals.MutableDecimal;

public final class MutableDecimalAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<MutableDecimal, MutableDecimalAllocator> {

    @Override
    protected MutableDecimal allocate(MutableDecimalAllocator allocator, int minimumCapacity) {

        return allocator.allocateArrayInstance(minimumCapacity);
    }

    @Override
    protected MutableDecimalAllocator createAllocator() {

        return new MutableDecimalAllocator();
    }

    @Override
    protected MutableDecimal[] allocateArray(int length) {

        return new MutableDecimal[length];
    }

    @Override
    protected void free(MutableDecimalAllocator allocator, MutableDecimal instance) {

        allocator.freeDecimal(instance);
    }
}
