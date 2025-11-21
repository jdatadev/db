package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableIntToObjectWithRemoveNonBucketMap<V> extends MutableIntToObjectWithRemoveNonBucketMap<V> implements IHeapMutableIntToObjectWithRemoveStaticMap<V> {

    HeapMutableIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);
    }
}
