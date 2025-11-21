package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.arrays.MutableLongArray;
import dev.jdata.db.utils.adt.arrays.CachedMutableLongArrayAllocator;
import dev.jdata.db.utils.allocators.BaseArrayMinimumCapacityAllocatorTest;

public final class MutableLongArrayAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<MutableLongArray, CachedMutableLongArrayAllocator> {

    @Override
    protected CachedMutableLongArrayAllocator createAllocator() {

        return new CachedMutableLongArrayAllocator();
    }

    @Override
    protected MutableLongArray allocate(CachedMutableLongArrayAllocator allocator, int minimumCapacity) {

        return allocator.allocateLongArray(minimumCapacity);
    }

    @Override
    protected MutableLongArray[] allocateArray(int length) {

        return new MutableLongArray[length];
    }

    @Override
    protected void free(CachedMutableLongArrayAllocator allocator, MutableLongArray instance) {

        allocator.freeLongArray(instance);
    }
}
