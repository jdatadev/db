package dev.jdata.db.utils.adt.numbers.integers;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapMutableLargeIntegerAllocator implements IHeapMutableLargeIntegerAllocator {

    @Override
    public IHeapMutableLargeInteger allocateLargeInteger(int precision) {

        return new HeapMutableLargeInteger(AllocationType.HEAP_ALLOCATOR, precision);
    }

    @Override
    public void freeLargeInteger(IHeapMutableLargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);
    }
}
