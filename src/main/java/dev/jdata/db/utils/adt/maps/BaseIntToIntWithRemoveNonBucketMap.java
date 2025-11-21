package dev.jdata.db.utils.adt.maps;

abstract class BaseIntToIntWithRemoveNonBucketMap extends BaseIntToIntNonContainsKeyNonBucketMap {

    BaseIntToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }

    BaseIntToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseIntToIntWithRemoveNonBucketMap(AllocationType allocationType, BaseIntToIntNonRemoveNonBucketMap toCopy) {
        super(allocationType, toCopy);
    }
}
