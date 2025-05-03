package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

abstract class BaseLongToObjectWithRemoveNonBucketMap<T> extends BaseLongToObjectNonContainsKeyNonBucketMap<T> {

    BaseLongToObjectWithRemoveNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, createArray);
    }

    BaseLongToObjectWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);
    }
}
