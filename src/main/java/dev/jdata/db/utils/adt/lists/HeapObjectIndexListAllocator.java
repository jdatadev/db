package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IntCapacityHeapElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapObjectIndexListAllocator<T>

        extends ObjectIndexListAllocator<T, IHeapIndexList<T>, IHeapIndexList<T>, IHeapMutableIndexList<T>, MutableObjectIndexList<T>, IHeapIndexListBuilder<T>>
        implements IHeapIndexListAllocator<T> {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

    HeapObjectIndexListAllocator(IntFunction<T[]> createElementsArray) {
        super(ALLOCATION_TYPE, new IntCapacityHeapElementsAllocators<>(ALLOCATION_TYPE, HeapObjectIndexList::copyArray, HeapObjectIndexList::empty,
                (a, c) -> new HeapMutableObjectIndexList<>(a, createElementsArray, c), (a, c) -> new HeapObjectIndexListBuilder<>(a, createElementsArray, c)));
    }
}
