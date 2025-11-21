package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableIndexListAllocator<T> extends MutableObjectIndexListAllocator implements IHeapMutableIndexListAllocator<T> {

    private final IntFunction<T[]> createElementsArray;

    HeapMutableIndexListAllocator(IntFunction<T[]> createElementsArray) {

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
    }

    @Override
    public IHeapMutableIndexList<T> createMutable(long minimumCapacity) {

        final int intMinimumCapacity = Capacity.intCapacity(minimumCapacity);

        Checks.isInitialCapacity(intMinimumCapacity);

        return new HeapMutableIndexList<>(AllocationType.HEAP_ALLOCATOR, createElementsArray, intMinimumCapacity);
    }

    @Override
    public void freeMutable(IHeapMutableIndexList<T> immutable) {

        Objects.requireNonNull(immutable);
    }

    @Override
    HeapMutableIndexList<T> allocateMutableIndexList(int minimumCapacity) {

        return new HeapMutableIndexList<>(AllocationType.HEAP_ALLOCATOR, createElementsArray);
    }

    @Override
    public void freeMutableIndexList(HeapMutableIndexList<T> list) {

    }
}
