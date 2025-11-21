package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityScalarMutableInstanceAllocator;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableIntIndexListAllocator

        extends IntCapacityScalarMutableInstanceAllocator<IHeapMutableIntIndexList, HeapMutableIntIndexList>
        implements IHeapMutableIntIndexListAllocator {

    static final HeapMutableIntIndexListAllocator INSTANCE = new HeapMutableIntIndexListAllocator();

    @Override
    protected HeapMutableIntIndexList allocateMutable(int minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return new HeapMutableIntIndexList(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }
}
