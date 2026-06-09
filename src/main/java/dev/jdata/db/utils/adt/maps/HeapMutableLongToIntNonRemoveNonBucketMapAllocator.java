package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityHeapScalarMutableInstanceAllocator;

final class HeapMutableLongToIntNonRemoveNonBucketMapAllocator

        extends IntCapacityHeapScalarMutableInstanceAllocator<IHeapMutableLongToIntNonRemoveStaticMap, HeapMutableLongToIntNonRemoveNonBucketMap, ILongToIntMapView>
        implements IHeapMutableLongToIntNonRemoveStaticMapAllocator {

    static final HeapMutableLongToIntNonRemoveNonBucketMapAllocator INSTANCE = new HeapMutableLongToIntNonRemoveNonBucketMapAllocator();

    private HeapMutableLongToIntNonRemoveNonBucketMapAllocator() {

    }

    @Override
    public IHeapMutableLongToIntNonRemoveStaticMap copyToMutable(ILongToIntMapView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }

    @Override
    protected HeapMutableLongToIntNonRemoveNonBucketMap allocateMutable(int minimumCapacity) {

        checkAllocateMutableParameters(minimumCapacity);

        return HeapMutableLongToIntNonRemoveNonBucketMap.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }
}
