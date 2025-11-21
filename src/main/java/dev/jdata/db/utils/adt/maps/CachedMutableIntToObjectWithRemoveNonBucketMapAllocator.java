package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableIntToObjectWithRemoveNonBucketMapAllocator<V>

        extends MutableIntToObjectWithRemoveNonBucketMapAllocator<V, ICachedMutableIntToObjectWithRemoveStaticMap<V>, CachedMutableIntToObjectWithRemoveNonBucketMap<V>>
        implements ICachedMutableIntToObjectWithRemoveStaticMapAllocator<V> {

    private final IntFunction<V[]> createValuesArray;

    CachedMutableIntToObjectWithRemoveNonBucketMapAllocator(IntFunction<V[]> createValuesArray) {

        this.createValuesArray = Objects.requireNonNull(createValuesArray);
    }

    @Override
    protected CachedMutableIntToObjectWithRemoveNonBucketMap<V> allocateMutableInstance(IntFunction<Void> createElements, int minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return CachedMutableIntToObjectWithRemoveNonBucketMap.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity, createValuesArray);
    }

    @Override
    public ICachedMutableIntToObjectWithRemoveStaticMap<V> copyToMutable(IIntToObjectMapView<V> mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }
}
