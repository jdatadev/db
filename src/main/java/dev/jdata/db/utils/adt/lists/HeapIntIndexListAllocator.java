package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IntCapacityHeapElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapIntIndexListAllocator

        extends IntIndexListAllocator<IHeapIntIndexList, IHeapIntIndexList, IHeapMutableIntIndexList, MutableIntIndexList, IHeapIntIndexListBuilder>
        implements IHeapIntIndexListAllocator {

    static final HeapIntIndexListAllocator INSTANCE = new HeapIntIndexListAllocator(AllocationType.HEAP_ALLOCATOR);

    private HeapIntIndexListAllocator(AllocationType allocationType) {
        super(allocationType, new IntCapacityHeapElementsAllocators<>(allocationType, HeapIntIndexList::copyArray, HeapIntIndexList::empty, HeapMutableIntIndexList::create,
                HeapIntIndexListBuilder::create));
    }
}
