package dev.jdata.db.utils.adt.maps;

abstract class BaseIntToIntNonRemoveNonBucketMap extends BaseIntToIntNonContainsKeyNonBucketMap {

    BaseIntToIntNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }

    BaseIntToIntNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseIntToIntNonRemoveNonBucketMap(AllocationType allocationType, BaseIntToIntNonRemoveNonBucketMap toCopy) {
        super(allocationType, toCopy);
    }
}
