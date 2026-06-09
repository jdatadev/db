package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IntCapacityHeapElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapIntSetAllocator extends IntSetAllocator<IHeapIntSet, IHeapIntSet, IHeapMutableIntSet, MutableIntMaxDistanceNonBucketSet, IHeapIntSetBuilder>

        implements IHeapIntSetAllocator {

    static final HeapIntSetAllocator INSTANCE = new HeapIntSetAllocator(AllocationType.HEAP_ALLOCATOR);

    private HeapIntSetAllocator(AllocationType allocationType) {
        super(allocationType, new IntCapacityHeapElementsAllocators<>(allocationType, HeapIntMaxDistanceNonBucketSet::copyArray, HeapIntMaxDistanceNonBucketSet::empty,
                HeapMutableIntMaxDistanceNonBucketSet::create, HeapIntSetBuilder::create));
    }
}
