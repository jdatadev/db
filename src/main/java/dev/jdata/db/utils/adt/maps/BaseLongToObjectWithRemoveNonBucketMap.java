package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

abstract class BaseLongToObjectWithRemoveNonBucketMap<T> extends BaseLongToObjectNonContainsKeyNonBucketMap<T> {

    BaseLongToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<T[]> createArray) {
        super(allocationType, initialCapacityExponent, createArray);
    }

    BaseLongToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<T[]> createArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);
    }
}
