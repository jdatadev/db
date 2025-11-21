package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityHeapScalarMutableInstanceAllocator;

final class HeapMutableLongSetAllocator

        extends IntCapacityHeapScalarMutableInstanceAllocator<IHeapMutableLongSet, HeapMutableLongMaxDistanceNonBucketSet, ILongIterableElementsView>
        implements IHeapMutableLongSetAllocator {

    static final HeapMutableLongSetAllocator INSTANCE = new HeapMutableLongSetAllocator();

    @Override
    protected HeapMutableLongMaxDistanceNonBucketSet allocateMutable(int minimumCapacity) {

        checkAllocateMutableParameters(minimumCapacity);

        return HeapMutableLongMaxDistanceNonBucketSet.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }

    @Override
    public HeapMutableLongMaxDistanceNonBucketSet copyToMutable(ILongIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        return HeapMutableLongMaxDistanceNonBucketSet.copyToMutable(AllocationType.HEAP_ALLOCATOR, mutableFrom);
    }
}
