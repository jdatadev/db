package dev.jdata.db.utils.adt.maps;

abstract class BaseIntToLongWithRemoveNonBucketMap extends BaseIntToLongNonContainsNonBucketMap implements IIntToLongKeyMapCommon {

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
