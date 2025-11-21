package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.BaseArrayMinimumCapacityAllocatorTest;

public final class MutableLongArrayAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<ICachedMutableLongArray, CachedMutableLongArrayAllocator> {

    @Override
    protected CachedMutableLongArrayAllocator createAllocator() {

        return new CachedMutableLongArrayAllocator();
    }

    @Override
    protected ICachedMutableLongArray allocate(CachedMutableLongArrayAllocator allocator, int minimumCapacity) {

        return allocator.createMutable(minimumCapacity);
    }

    @Override
    protected ICachedMutableLongArray[] allocateArray(int length) {

        return new ICachedMutableLongArray[length];
    }

    @Override
    protected void free(CachedMutableLongArrayAllocator allocator, ICachedMutableLongArray instance) {

        allocator.freeMutable(instance);
    }
}
