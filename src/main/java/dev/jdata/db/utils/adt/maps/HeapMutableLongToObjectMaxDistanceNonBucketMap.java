package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableLongToObjectMaxDistanceNonBucketMap<V>

        extends MutableLongToObjectMaxDistanceNonBucketMap<V, HeapMutableLongToObjectMaxDistanceNonBucketMap<V>>
        implements IHeapMutableLongToObjectDynamicMap<V> {

    static <V> HeapMutableLongToObjectMaxDistanceNonBucketMap<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createValuesArray, HeapMutableLongToObjectMaxDistanceNonBucketMap::new);
    }

    private HeapMutableLongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }
}
