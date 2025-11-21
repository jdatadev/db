package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableIntToObjectMaxDistanceNonBucketMap<V> extends MutableIntToObjectMaxDistanceNonBucketMap<V> implements IHeapMutableIntToObjectDynamicMap<V> {

    HeapMutableIntToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);
    }
}
