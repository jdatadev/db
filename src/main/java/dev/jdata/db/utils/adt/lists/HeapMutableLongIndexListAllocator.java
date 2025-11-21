package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityScalarMutableInstanceAllocator;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableLongIndexListAllocator

        extends IntCapacityScalarMutableInstanceAllocator<IHeapMutableLongIndexList, HeapMutableLongIndexList>
        implements IHeapMutableLongIndexListAllocator {

    static final HeapMutableLongIndexListAllocator INSTANCE = new HeapMutableLongIndexListAllocator();

    @Override
    protected HeapMutableLongIndexList allocateMutable(int minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return new HeapMutableLongIndexList(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }
}
