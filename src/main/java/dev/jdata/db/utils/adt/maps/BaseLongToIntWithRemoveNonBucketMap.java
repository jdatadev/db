package dev.jdata.db.utils.adt.maps;

abstract class BaseLongToIntWithRemoveNonBucketMap extends BaseLongToIntNonContainsKeyNonBucketMap {

    BaseLongToIntWithRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseLongToIntWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToIntWithRemoveNonBucketMap(BaseLongToIntNonContainsKeyNonBucketMap toCopy) {
        super(toCopy);
    }
}
