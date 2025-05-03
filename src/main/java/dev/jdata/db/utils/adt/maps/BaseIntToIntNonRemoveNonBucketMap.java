package dev.jdata.db.utils.adt.maps;

abstract class BaseIntToIntNonRemoveNonBucketMap extends BaseIntToIntNonContainsKeyNonBucketMap {

    BaseIntToIntNonRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseIntToIntNonRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseIntToIntNonRemoveNonBucketMap(BaseIntToIntNonRemoveNonBucketMap toCopy) {
        super(toCopy);
    }
}
