package dev.jdata.db.utils.adt.maps;

abstract class BaseIntToIntWithRemoveNonBucketMap extends BaseIntToIntNonContainsKeyNonBucketMap {

    BaseIntToIntWithRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseIntToIntWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseIntToIntWithRemoveNonBucketMap(BaseIntToIntNonRemoveNonBucketMap toCopy) {
        super(toCopy);
    }
}
