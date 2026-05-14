package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapMutableLongLargeSetAllocator

        extends BaseLongCapacityMutableSetAllocator<IHeapMutableLongLargeSet, HeapMutableLongMaxDistanceNonBucketLargeSet, ILongIterableElementsView>
        implements IHeapMutableLongLargeSetAllocator {

    static final HeapMutableLongLargeSetAllocator INSTANCE = new HeapMutableLongLargeSetAllocator();

    private HeapMutableLongLargeSetAllocator() {

    }

    @Override
    protected HeapMutableLongMaxDistanceNonBucketLargeSet allocateMutableLargeSet(long minimumCapacity) {

        return HeapMutableLongMaxDistanceNonBucketLargeSet.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }
}
