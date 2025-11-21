package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityHeapScalarMutableInstanceAllocator;

final class HeapMutableIntSetAllocator

        extends IntCapacityHeapScalarMutableInstanceAllocator<IHeapMutableIntSet, HeapMutableIntMaxDistanceNonBucketSet, IIntIterableElementsView>
        implements IHeapMutableIntSetAllocator {

    static final HeapMutableIntSetAllocator INSTANCE = new HeapMutableIntSetAllocator();

    @Override
    protected HeapMutableIntMaxDistanceNonBucketSet allocateMutable(int minimumCapacity) {

        checkAllocateMutableParameters(minimumCapacity);

        return HeapMutableIntMaxDistanceNonBucketSet.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }

    @Override
    public HeapMutableIntMaxDistanceNonBucketSet copyToMutable(IIntIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        return HeapMutableIntMaxDistanceNonBucketSet.copyToMutable(AllocationType.HEAP_ALLOCATOR, mutableFrom);
    }
}
