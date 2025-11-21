package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableObjectWithRemoveNonBucketMap<K, V> extends MutableObjectWithRemoveNonBucketMap<K, V> implements IHeapMutableWithRemoveStaticMap<K, V> {

    HeapMutableObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createKeysArray, createValuesArray);
    }
}
