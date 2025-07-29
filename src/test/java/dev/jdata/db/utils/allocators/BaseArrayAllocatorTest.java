package dev.jdata.db.utils.allocators;

abstract class BaseArrayAllocatorTest<T, A> extends BaseAllocatorTest<T, A> {

    protected abstract T allocate(A allocator, int minimumCapacity);

    @Override
    T allocate(A allocator) {

        return allocate(allocator, 1);
    }

    @Override
    final boolean freesInSameOrder() {

        return true;
    }
}
