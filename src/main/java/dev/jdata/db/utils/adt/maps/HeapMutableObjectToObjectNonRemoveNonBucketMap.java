package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableObjectToObjectNonRemoveNonBucketMap<K, V> extends MutableObjectToObjectNonRemoveNonBucketMap<K, V> implements IHeapMutableNonRemoveStaticMap<K, V> {

    static <K, V> HeapMutableObjectToObjectNonRemoveNonBucketMap<K, V> create(AllocationType allocationType, int initialCapacity, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createKeysArray, createValuesArray,
                HeapMutableObjectToObjectNonRemoveNonBucketMap::new);
    }

    HeapMutableObjectToObjectNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);
    }
}
