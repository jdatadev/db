package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

abstract class BaseIntToObjectWithRemoveNonBucketMap<V, M extends BaseIntToObjectWithRemoveNonBucketMap<V, M>> extends BaseIntToObjectNonContainsKeyNonBucketMap<V, M> {

    BaseIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, BaseIntToObjectWithRemoveNonBucketMap<V, M> toCopy) {
        super(allocationType, toCopy);
    }
}
