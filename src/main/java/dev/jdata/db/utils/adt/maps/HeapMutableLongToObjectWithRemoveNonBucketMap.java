package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableLongToObjectWithRemoveNonBucketMap<V> extends MutableLongToObjectWithRemoveNonBucketMap<V> implements IHeapMutableLongToObjectWithRemoveStaticMap<V> {

    HeapMutableLongToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);
    }
}
