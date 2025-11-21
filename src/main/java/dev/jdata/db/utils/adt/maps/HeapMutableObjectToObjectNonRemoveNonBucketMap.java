package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableObjectToObjectNonRemoveNonBucketMap<K, V> extends MutableObjectToObjectNonRemoveNonBucketMap<K, V> implements IHeapMutableNonRemoveStaticMap<K, V> {

    HeapMutableObjectToObjectNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createKeysArray, createValuesArray);
    }
}
