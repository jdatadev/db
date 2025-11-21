package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

abstract class BaseIntToObjectWithRemoveNonBucketMap<T> extends BaseIntToObjectNonContainsKeyNonBucketMap<T> {

    BaseIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);
    }

    BaseIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<T[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, BaseIntToObjectWithRemoveNonBucketMap<T> toCopy) {
        super(allocationType, toCopy);
    }
}
