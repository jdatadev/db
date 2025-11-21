package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableObjectToObjectWithRemoveNonBucketMap<K, V> extends MutableObjectWithRemoveNonBucketMap<K, V> implements IHeapMutableWithRemoveStaticMap<K, V> {

    static <K, V> HeapMutableObjectToObjectWithRemoveNonBucketMap<K, V> create(AllocationType allocationType, int initialCapacity, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createKeysArray, createValuesArray,
                HeapMutableObjectToObjectWithRemoveNonBucketMap::new);
    }

    private HeapMutableObjectToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);
    }
}
