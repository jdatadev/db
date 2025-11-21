package dev.jdata.db.utils.adt.maps;

abstract class BaseLongToLongWithRemoveNonBucketMap extends BaseLongToLongNonContainsKeyNonBucketMap {

    BaseLongToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }

    BaseLongToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToLongWithRemoveNonBucketMap(AllocationType allocationType, BaseLongToLongNonContainsKeyNonBucketMap toCopy) {
        super(allocationType, toCopy);
    }
}
