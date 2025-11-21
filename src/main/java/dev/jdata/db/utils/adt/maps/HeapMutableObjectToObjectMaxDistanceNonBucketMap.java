package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableObjectToObjectMaxDistanceNonBucketMap<K, V> extends MutableObjectMaxDistanceNonBucketMap<K, V> implements IHeapMutableDynamicMap<K, V> {

    static <K, V> HeapMutableObjectToObjectMaxDistanceNonBucketMap<K, V> create(AllocationType allocationType, int initialCapacity, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createKeysArray, createValuesArray,
                HeapMutableObjectToObjectMaxDistanceNonBucketMap::new);
    }

    private HeapMutableObjectToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);
    }
}
