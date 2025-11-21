package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityObjectHeapMutableInstanceAllocator;

final class HeapMutableObjectIndexListAllocator<T>

        extends IntCapacityObjectHeapMutableInstanceAllocator<IHeapMutableIndexList<T>, HeapMutableObjectIndexList<T>, T[], IObjectIterableElementsView<T>>
        implements IHeapMutableIndexListAllocator<T> {

    private final IntFunction<T[]> createElementsArray;

    @Deprecated // fix for copyToMutable()
    HeapMutableObjectIndexListAllocator(IntFunction<T[]> createElementsArray) {
        super(createElementsArray);

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
    }

    @Override
    protected HeapMutableObjectIndexList<T> allocateMutable(IntFunction<T[]> createElementsArray, int minimumCapacity) {

        return HeapMutableObjectIndexList.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity, createElementsArray);
    }

    @Override
    public IHeapMutableIndexList<T> copyToMutable(IObjectIterableElementsView<T> mutableFrom) {

        return HeapMutableObjectIndexList.copyToMutable(AllocationType.HEAP_ALLOCATOR, mutableFrom, createElementsArray);
    }
}
