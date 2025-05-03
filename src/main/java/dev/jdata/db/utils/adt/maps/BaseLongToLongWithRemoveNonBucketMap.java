package dev.jdata.db.utils.adt.maps;

abstract class BaseLongToLongWithRemoveNonBucketMap extends BaseLongToLongNonContainsKeyNonBucketMap {

    BaseLongToLongWithRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseLongToLongWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToLongWithRemoveNonBucketMap(BaseLongToLongNonContainsKeyNonBucketMap toCopy) {
        super(toCopy);
    }
}
