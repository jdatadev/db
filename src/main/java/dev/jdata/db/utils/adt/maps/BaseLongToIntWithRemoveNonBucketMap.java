package dev.jdata.db.utils.adt.maps;

@Deprecated // necessary? always mutable
abstract class BaseLongToIntWithRemoveNonBucketMap<M extends BaseLongToIntWithRemoveNonBucketMap<M>> extends BaseLongToIntNonContainsKeyNonBucketMap<M> {

    BaseLongToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToIntWithRemoveNonBucketMap(AllocationType allocationType, BaseLongToIntNonContainsKeyNonBucketMap<M> toCopy) {
        super(allocationType, toCopy);
    }
}
