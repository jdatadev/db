package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.decimals.MutableDecimal;

public final class MutableDecimalAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<MutableDecimal, MutableDecimalAllocator> {

    @Override
    MutableDecimal allocate(MutableDecimalAllocator allocator, int minimumCapacity) {

        return allocator.allocateArrayInstance(minimumCapacity);
    }

    @Override
    MutableDecimalAllocator createAllocator() {

        return new MutableDecimalAllocator();
    }

    @Override
    MutableDecimal[] allocateArray(int length) {

        return new MutableDecimal[length];
    }

    @Override
    void free(MutableDecimalAllocator allocator, MutableDecimal instance) {

        allocator.freeDecimal(instance);
    }
}
