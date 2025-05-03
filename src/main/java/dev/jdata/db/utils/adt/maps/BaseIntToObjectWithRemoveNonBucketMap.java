package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

abstract class BaseIntToObjectWithRemoveNonBucketMap<T> extends BaseIntToObjectNonContainsKeyNonBucketMap<T> {

    BaseIntToObjectWithRemoveNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, createValuesArray);
    }

    BaseIntToObjectWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseIntToObjectWithRemoveNonBucketMap(BaseIntToObjectWithRemoveNonBucketMap<T> toCopy) {
        super(toCopy);
    }
}
