package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityObjectHeapMutableInstanceAllocator;

final class HeapMutableLongToObjectDynamicMapAllocator<V>

        extends IntCapacityObjectHeapMutableInstanceAllocator<

                        IHeapMutableLongToObjectDynamicMap<V>,
                        HeapMutableLongToObjectMaxDistanceNonBucketMap<V>,
                        V[],
                        ILongToObjectMapView<V>>

        implements IHeapMutableLongToObjectDynamicMapAllocator<V> {

    static <V> HeapMutableLongToObjectDynamicMapAllocator<V> create(IntFunction<V[]> createValuesArray) {

        return new HeapMutableLongToObjectDynamicMapAllocator<V>(createValuesArray);
    }

    private HeapMutableLongToObjectDynamicMapAllocator(IntFunction<V[]> createValuesArray) {
        super(createValuesArray);
    }

    @Override
    protected HeapMutableLongToObjectMaxDistanceNonBucketMap<V> allocateMutable(IntFunction<V[]> createElements, int minimumCapacity) {

        return HeapMutableLongToObjectMaxDistanceNonBucketMap.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity, createElements);
    }

    @Override
    public IHeapMutableLongToObjectDynamicMap<V> copyToMutable(ILongToObjectMapView<V> mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }
}
