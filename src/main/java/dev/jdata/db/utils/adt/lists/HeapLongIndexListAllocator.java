package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IntCapacityHeapElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapLongIndexListAllocator

        extends LongIndexListAllocator<IHeapLongIndexList, IHeapLongIndexList, IHeapMutableLongIndexList, MutableLongIndexList, IHeapLongIndexListBuilder>
        implements IHeapLongIndexListAllocator {

    static final HeapLongIndexListAllocator INSTANCE = new HeapLongIndexListAllocator(AllocationType.HEAP_ALLOCATOR);

    private HeapLongIndexListAllocator(AllocationType allocationType) {
        super(allocationType, new IntCapacityHeapElementsAllocators<>(allocationType, HeapLongIndexList::copyArray, HeapLongIndexList::empty, HeapMutableLongIndexList::create,
                HeapLongIndexListBuilder::create));
    }
}
