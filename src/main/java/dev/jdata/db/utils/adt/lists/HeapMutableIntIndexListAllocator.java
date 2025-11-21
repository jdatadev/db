package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityHeapScalarMutableInstanceAllocator;

final class HeapMutableIntIndexListAllocator

        extends IntCapacityHeapScalarMutableInstanceAllocator<IHeapMutableIntIndexList, HeapMutableIntIndexList, IIntIterableElementsView>
        implements IHeapMutableIntIndexListAllocator {

    static final HeapMutableIntIndexListAllocator INSTANCE = new HeapMutableIntIndexListAllocator();

    private HeapMutableIntIndexListAllocator() {

    }

    @Override
    protected HeapMutableIntIndexList allocateMutable(int minimumCapacity) {

        checkAllocateMutableParameters(minimumCapacity);

        return HeapMutableIntIndexList.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }

    @Override
    public IHeapMutableIntIndexList copyToMutable(IIntIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        return HeapMutableIntIndexList.copyToMutable(AllocationType.HEAP_ALLOCATOR, mutableFrom);
    }
}
