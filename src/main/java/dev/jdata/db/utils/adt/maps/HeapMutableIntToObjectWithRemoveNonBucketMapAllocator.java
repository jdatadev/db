package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IntCapacityObjectHeapMutableInstanceAllocator;

final class HeapMutableIntToObjectWithRemoveNonBucketMapAllocator<V>

        extends IntCapacityObjectHeapMutableInstanceAllocator<
                        IHeapMutableIntToObjectWithRemoveStaticMap<V>,
                        HeapMutableIntToObjectWithRemoveNonBucketMap<V>,
                        V[],
                        IIntToObjectMapView<V>>
        implements IHeapMutableIntToObjectWithRemoveStaticMapAllocator<V> {

    static <V> HeapMutableIntToObjectWithRemoveNonBucketMapAllocator<V> create(IntFunction<V[]> createValuesArray) {

        Objects.requireNonNull(createValuesArray);

        return new HeapMutableIntToObjectWithRemoveNonBucketMapAllocator<>(createValuesArray);
    }

    private HeapMutableIntToObjectWithRemoveNonBucketMapAllocator(IntFunction<V[]> createValuesArray) {
        super(createValuesArray);
    }

    @Override
    protected HeapMutableIntToObjectWithRemoveNonBucketMap<V> allocateMutable(IntFunction<V[]> createElements, int minimumCapacity) {

        return HeapMutableIntToObjectWithRemoveNonBucketMap.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity, createElements);
    }

    @Override
    public IHeapMutableIntToObjectWithRemoveStaticMap<V> copyToMutable(IIntToObjectMapView<V> mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }
}
