package dev.jdata.db.utils.adt.numbers.integers;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapMutableLargeIntegerAllocator implements IHeapMutableLargeIntegerAllocator {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

    @Override
    public IHeapMutableLargeInteger allocateMutableLargeInteger(int precision) {

        return HeapMutableLargeInteger.create(ALLOCATION_TYPE, precision);
    }

    @Override
    public IHeapMutableLargeInteger allocateMutableLargeInteger(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        return HeapMutableLargeInteger.create(ALLOCATION_TYPE, largeInteger);
    }

    @Override
    public void freeMutableLargeInteger(IHeapMutableLargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);
    }
}
