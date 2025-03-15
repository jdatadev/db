package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.arrays.LongArray;

public final class LongArrayAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<LongArray, LongArrayAllocator> {

    @Override
    LongArrayAllocator createAllocator() {

        return new LongArrayAllocator();
    }

    @Override
    LongArray allocate(LongArrayAllocator allocator, int minimumCapacity) {

        return allocator.allocateLongArray(minimumCapacity);
    }

    @Override
    LongArray[] allocateArray(int length) {

        return new LongArray[length];
    }

    @Override
    void free(LongArrayAllocator allocator, LongArray instance) {

        allocator.freeLongArray(instance);
    }
}
