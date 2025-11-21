package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IntCapacityHeapElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapIntSetAllocator extends IntSetAllocator<IHeapIntSet, IHeapIntSet, IHeapMutableIntSet, MutableIntMaxDistanceNonBucketSet, IHeapIntSetBuilder>

        implements IHeapIntSetAllocator {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

    HeapIntSetAllocator() {
        super(ALLOCATION_TYPE, new IntCapacityHeapElementsAllocators<>(ALLOCATION_TYPE, HeapIntMaxDistanceNonBucketSet::copyArray, HeapIntMaxDistanceNonBucketSet::empty,
                HeapMutableIntMaxDistanceNonBucketSet::create, HeapIntSetBuilder::new));
    }
}
