package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapMutableLongLargeArrayAllocator

        extends HeapMutableLargeArrayAllocator<IHeapMutableLongLargeArray, HeapMutableLongLargeArray, ILongIterableElementsView>
        implements IHeapMutableLongLargeArrayAllocator {

    static final HeapMutableLongLargeArrayAllocator INSTANCE = new HeapMutableLongLargeArrayAllocator();

    private HeapMutableLongLargeArrayAllocator() {

    }

    @Override
    HeapMutableLongLargeArray allocateMutableLargeArray(long minimumCapacity) {

        return HeapMutableLongLargeArray.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }
}
