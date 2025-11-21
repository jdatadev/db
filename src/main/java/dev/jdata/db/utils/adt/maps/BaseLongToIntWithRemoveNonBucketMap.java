package dev.jdata.db.utils.adt.maps;

abstract class BaseLongToIntWithRemoveNonBucketMap extends BaseLongToIntNonContainsKeyNonBucketMap {

    BaseLongToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }

    BaseLongToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToIntWithRemoveNonBucketMap(AllocationType allocationType, BaseLongToIntNonContainsKeyNonBucketMap toCopy) {
        super(allocationType, toCopy);
    }
}
