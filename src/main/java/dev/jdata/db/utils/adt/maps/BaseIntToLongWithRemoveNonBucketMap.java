package dev.jdata.db.utils.adt.maps;

abstract class BaseIntToLongWithRemoveNonBucketMap extends BaseIntToLongNonContainsNonBucketMap implements IIntToLongKeyMapCommon {

    BaseIntToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }

    BaseIntToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseIntToLongWithRemoveNonBucketMap(AllocationType allocationType, BaseIntToLongWithRemoveNonBucketMap toCopy) {
        super(allocationType, toCopy);
    }
}
