package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityScalarMutableInstanceAllocator;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableIntSetAllocator

        extends IntCapacityScalarMutableInstanceAllocator<IHeapMutableIntSet, HeapMutableIntMaxDistanceNonBucketSet>
        implements IHeapMutableIntSetAllocator {

    static final HeapMutableIntSetAllocator INSTANCE = new HeapMutableIntSetAllocator();

    @Override
    protected HeapMutableIntMaxDistanceNonBucketSet allocateMutable(int minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return HeapMutableIntMaxDistanceNonBucketSet.create(AllocationType.HEAP, minimumCapacity);
    }
}
