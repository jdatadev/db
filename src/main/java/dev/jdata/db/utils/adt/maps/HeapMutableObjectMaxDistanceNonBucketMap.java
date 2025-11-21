package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableObjectMaxDistanceNonBucketMap<K, V> extends MutableObjectMaxDistanceNonBucketMap<K, V> implements IHeapMutableDynamicMap<K, V> {

    HeapMutableObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createKeysArray, createValuesArray);
    }

    HeapMutableObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);
    }
}
