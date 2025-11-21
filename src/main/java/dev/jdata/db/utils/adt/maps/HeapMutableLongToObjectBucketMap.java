package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableLongToObjectBucketMap<T> extends MutableLongToObjectBucketMap<T> {

    HeapMutableLongToObjectBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<T[][]> createOuterValuesArray, IntFunction<T[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createOuterValuesArray, createValuesArray);
    }
}
