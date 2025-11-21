package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityObjectHeapMutableInstanceAllocator;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableObjectIndexListAllocator<T>

        extends IntCapacityObjectHeapMutableInstanceAllocator<IHeapMutableIndexList<T>, T[]>
        implements IHeapMutableIndexListAllocator<T> {

    HeapMutableObjectIndexListAllocator(IntFunction<T[]> createElementsArray) {
        super(createElementsArray);
    }

    @Override
    protected IHeapMutableIndexList<T> allocateMutable(IntFunction<T[]> createElementsArray, int minimumCapacity) {

        Objects.requireNonNull(createElementsArray);
        Checks.isIntMinimumCapacity(minimumCapacity);

        return new HeapMutableObjectIndexList<>(AllocationType.HEAP_ALLOCATOR, createElementsArray, minimumCapacity);
    }
}
