package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IntCapacityHeapElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapLongIndexListAllocator

        extends LongIndexListAllocator<IHeapLongIndexList, IHeapLongIndexList, IHeapMutableLongIndexList, MutableLongIndexList, IHeapLongIndexListBuilder>
        implements IHeapLongIndexListAllocator {

    private static final HeapLongIndexListAllocator INSTANCE = new HeapLongIndexListAllocator();

    private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

    private HeapLongIndexListAllocator() {
        super(ALLOCATION_TYPE, new IntCapacityHeapElementsAllocators<>(ALLOCATION_TYPE, HeapLongIndexList::copyArray, HeapLongIndexList::empty, HeapMutableLongIndexList::create,
                HeapLongIndexListBuilder::create));
    }
}
