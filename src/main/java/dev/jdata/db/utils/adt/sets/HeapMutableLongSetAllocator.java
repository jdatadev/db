package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityScalarMutableInstanceAllocator;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableLongSetAllocator extends IntCapacityScalarMutableInstanceAllocator<IHeapMutableLongSet> implements IHeapMutableLongSetAllocator {

    static final HeapMutableLongSetAllocator INSTANCE = new HeapMutableLongSetAllocator();

    @Override
    protected IHeapMutableLongSet allocateMutable(int minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return new HeapMutableLongMaxDistanceNonBucketSet(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }
}
