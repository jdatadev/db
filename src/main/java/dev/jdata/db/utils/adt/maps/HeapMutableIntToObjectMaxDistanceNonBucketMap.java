package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableIntToObjectMaxDistanceNonBucketMap<V> extends MutableIntToObjectMaxDistanceNonBucketMap<V> implements IHeapMutableIntToObjectDynamicMap<V> {

    static <V> HeapMutableIntToObjectMaxDistanceNonBucketMap<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createValuesArray, HeapMutableIntToObjectMaxDistanceNonBucketMap::new);
    }

    private HeapMutableIntToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }
}
