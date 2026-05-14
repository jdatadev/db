package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableLongToObjectDynamicMapAllocator<V>

        extends MutableLongToObjectDynamicMapAllocator<V, ICachedMutableLongToObjectDynamicMap<V>, CachedMutableLongToObjectMaxDistanceNonBucketMap<V>>
        implements ICachedMutableLongToObjectDynamicMapAllocator<V> {

    private final IntFunction<V[]> createValuesArray;

    CachedMutableLongToObjectDynamicMapAllocator(IntFunction<V[]> createValuesArray) {

        this.createValuesArray = Objects.requireNonNull(createValuesArray);
    }

    @Override
    protected CachedMutableLongToObjectMaxDistanceNonBucketMap<V> allocateMutableInstance(IntFunction<Void> createElements, int minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return CachedMutableLongToObjectMaxDistanceNonBucketMap.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity, createValuesArray);
    }

    @Override
    public ICachedMutableLongToObjectDynamicMap<V> copyToMutable(ILongToObjectMapView<V> mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }
}
