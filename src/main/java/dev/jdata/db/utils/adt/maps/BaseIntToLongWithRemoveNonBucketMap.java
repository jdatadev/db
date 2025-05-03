package dev.jdata.db.utils.adt.maps;

abstract class BaseIntToLongWithRemoveNonBucketMap extends BaseIntToLongNonContainsNonBucketMap implements IIntKeyMap {

    BaseIntToLongWithRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseIntToLongWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseIntToLongWithRemoveNonBucketMap(BaseIntToLongWithRemoveNonBucketMap toCopy) {
        super(toCopy);
    }
}
