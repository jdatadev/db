package dev.jdata.db.utils.allocators;

import java.util.List;

public final class ListAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<List<StringBuilder>, ListAllocator> {

    @Override
    ListAllocator createAllocator() {

        return new ListAllocator(StringBuilder[]::new);
    }

    @Override
    List<StringBuilder> allocate(ListAllocator allocator, int minimumCapacity) {

        return allocator.allocateList(minimumCapacity);
    }

    @SuppressWarnings("unchecked")
    @Override
    List<StringBuilder>[] allocateArray(int length) {

        return new List[length];
    }

    @Override
    void free(ListAllocator allocator, List<StringBuilder> instance) {

        allocator.freeList(instance);
    }
}
