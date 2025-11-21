package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.arrays.MutableLongArray;
import dev.jdata.db.utils.adt.arrays.MutableLongArrayAllocator;
import dev.jdata.db.utils.allocators.BaseArrayMinimumCapacityAllocatorTest;

public final class MutableLongArrayAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<MutableLongArray, MutableLongArrayAllocator> {

    @Override
    protected MutableLongArrayAllocator createAllocator() {

        return new MutableLongArrayAllocator();
    }

    @Override
    protected MutableLongArray allocate(MutableLongArrayAllocator allocator, int minimumCapacity) {

        return allocator.allocateLongArray(minimumCapacity);
    }

    @Override
    protected MutableLongArray[] allocateArray(int length) {

        return new MutableLongArray[length];
    }

    @Override
    protected void free(MutableLongArrayAllocator allocator, MutableLongArray instance) {

        allocator.freeLongArray(instance);
    }
}
