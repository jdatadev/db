package dev.jdata.db.utils.jdk.allocators;

import java.util.List;

import dev.jdata.db.utils.allocators.BaseArrayMinimumCapacityAllocatorTest;

public final class ListAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<List<StringBuilder>, ListAllocator> {

    @Override
    protected ListAllocator createAllocator() {

        return new ListAllocator(StringBuilder[]::new);
    }

    @Override
    protected List<StringBuilder> allocate(ListAllocator allocator, int minimumCapacity) {

        return allocator.allocateList(minimumCapacity);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<StringBuilder>[] allocateArray(int length) {

        return new List[length];
    }

    @Override
    protected void free(ListAllocator allocator, List<StringBuilder> instance) {

        allocator.freeList(instance);
    }
}
