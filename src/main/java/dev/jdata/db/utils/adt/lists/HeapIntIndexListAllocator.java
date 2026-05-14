package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IntCapacityHeapElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapIntIndexListAllocator

        extends IntIndexListAllocator<IHeapIntIndexList, IHeapIntIndexList, IHeapMutableIntIndexList, MutableIntIndexList, IHeapIntIndexListBuilder>
        implements IHeapIntIndexListAllocator {

    static final HeapIntIndexListAllocator INSTANCE = new HeapIntIndexListAllocator();

    private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

    private HeapIntIndexListAllocator() {
        super(ALLOCATION_TYPE, new IntCapacityHeapElementsAllocators<>(ALLOCATION_TYPE, HeapIntIndexList::copyArray, HeapIntIndexList::empty, HeapMutableIntIndexList::create,
                HeapIntIndexListBuilder::create));
    }
}
