package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface ICachedMutableIntToObjectWithRemoveStaticMapAllocator<V>

        extends IMutableIntToObjectWithRemoveStaticMapAllocator<V, ICachedMutableIntToObjectWithRemoveStaticMap<V>> {

    public static <V> ICachedMutableIntToObjectWithRemoveStaticMapAllocator<V> create(IntFunction<V[]> createValuesArray) {

        Objects.requireNonNull(createValuesArray);

        return new CachedMutableIntToObjectWithRemoveNonBucketMap<V>(AllocationType.CACHING_ALLOCATOR, ADTConstants.DEFAULT_HASHED_INITIAL_CAPACITY_EXPONENT, createValuesArray);
    }
/*
    public static <V> ICachedMutableIntToObjectWithRemoveStaticMapAllocator<V> create(int minimumCapacity, IntFunction<V[]> createValuesArray) {

        Checks.isIntMinimumCapacity(minimumCapacity);
        Objects.requireNonNull(createValuesArray);

        return new CachedMutableIntToObjectWithRemoveNonBucketMap<V>(AllocationType.CACHING_ALLOCATOR, CapacityExponents.computeIntCapacityExponent(minimumCapacity),
                createValuesArray);
    }
*/
}
