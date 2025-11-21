package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class CachedMutableIntToObjectWithRemoveNonBucketMap<V>

        extends MutableIntToObjectWithRemoveNonBucketMap<V, CachedMutableIntToObjectWithRemoveNonBucketMap<V>>
        implements ICachedMutableIntToObjectWithRemoveStaticMap<V> {

    static <V> CachedMutableIntToObjectWithRemoveNonBucketMap<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.CACHE, initialCapacity, createValuesArray,
                CachedMutableIntToObjectWithRemoveNonBucketMap::new);
    }

    private CachedMutableIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }
}
