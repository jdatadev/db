package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityHeapScalarMutableInstanceAllocator;

final class HeapMutableLongIndexListAllocator

        extends IntCapacityHeapScalarMutableInstanceAllocator<IHeapMutableLongIndexList, HeapMutableLongIndexList, ILongIterableElementsView>
        implements IHeapMutableLongIndexListAllocator {

    static final HeapMutableLongIndexListAllocator INSTANCE = new HeapMutableLongIndexListAllocator();

    private HeapMutableLongIndexListAllocator() {

    }

    @Override
    protected HeapMutableLongIndexList allocateMutable(int minimumCapacity) {

        checkAllocateMutableParameters(minimumCapacity);

        return HeapMutableLongIndexList.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }

    @Override
    public IHeapMutableLongIndexList copyToMutable(ILongIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        return HeapMutableLongIndexList.copyToMutable(AllocationType.HEAP_ALLOCATOR, mutableFrom);
    }
}
