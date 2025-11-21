package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class CachedMutableLongToObjectMaxDistanceNonBucketMap<V>

        extends MutableLongToObjectMaxDistanceNonBucketMap<V, CachedMutableLongToObjectMaxDistanceNonBucketMap<V>>
        implements ICachedMutableLongToObjectDynamicMap<V> {

    static <V> CachedMutableLongToObjectMaxDistanceNonBucketMap<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.CACHE, initialCapacity, createValuesArray,
                CachedMutableLongToObjectMaxDistanceNonBucketMap::new);
    }

    CachedMutableLongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }
}
