package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityObjectHeapMutableInstanceAllocator;

final class HeapMutableLongToIntNonRemoveNonBucketMapAllocator

        extends IntCapacityObjectHeapMutableInstanceAllocator<IHeapMutableLongToIntNonRemoveStaticMap, HeapMutableLongToIntNonRemoveNonBucketMap, int[], ILongToIntMapView>
        implements IHeapMutableLongToIntNonRemoveStaticMapAllocator {

    static final HeapMutableLongToIntNonRemoveNonBucketMapAllocator INSTANCE = new HeapMutableLongToIntNonRemoveNonBucketMapAllocator();

    private HeapMutableLongToIntNonRemoveNonBucketMapAllocator() {
        super(int[]::new);
    }

    @Override
    protected HeapMutableLongToIntNonRemoveNonBucketMap allocateMutable(IntFunction<int[]> createElements, int minimumCapacity) {

        return allocateMutable(minimumCapacity);
    }

    HeapMutableLongToIntNonRemoveNonBucketMap allocateMutable(int minimumCapacity) {

        return HeapMutableLongToIntNonRemoveNonBucketMap.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
    }

    @Override
    public IHeapMutableLongToIntNonRemoveStaticMap copyToMutable(ILongToIntMapView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }
}
