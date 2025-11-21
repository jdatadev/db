package dev.jdata.db.utils.adt.maps;

abstract class BaseLongToIntNonRemoveNonBucketMap<M extends BaseLongToIntNonRemoveNonBucketMap<M>> extends BaseLongToIntNonContainsKeyNonBucketMap<M> {

    BaseLongToIntNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToIntNonRemoveNonBucketMap(AllocationType allocationType, BaseLongToIntNonRemoveNonBucketMap<M> toCopy) {
        super(allocationType, toCopy);
    }
}
