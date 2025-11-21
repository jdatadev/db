package dev.jdata.db.utils.adt.maps;

abstract class LongToIntNonContainsKeyNonBucketMap<M extends LongToIntNonContainsKeyNonBucketMap<M>>

        extends BaseLongToIntNonContainsKeyNonBucketMap<M>
        implements ILongToIntStaticMap  {

    LongToIntNonContainsKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    LongToIntNonContainsKeyNonBucketMap(AllocationType allocationType, BaseLongToIntNonBucketMap<?> toInitializeFrom, Void disambiguate) {
        super(allocationType, toInitializeFrom, disambiguate);
    }

    LongToIntNonContainsKeyNonBucketMap(AllocationType allocationType, LongToIntNonContainsKeyNonBucketMap<?> toCopy) {
        super(allocationType, toCopy);
    }
}
