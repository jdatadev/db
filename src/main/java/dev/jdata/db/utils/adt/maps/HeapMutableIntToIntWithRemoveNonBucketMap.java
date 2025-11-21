package dev.jdata.db.utils.adt.maps;

final class HeapMutableIntToIntWithRemoveNonBucketMap extends MutableIntToIntWithRemoveNonBucketMap {

    HeapMutableIntToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }

    HeapMutableIntToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    HeapMutableIntToIntWithRemoveNonBucketMap(AllocationType allocationType, MutableIntToIntNonRemoveNonBucketMap toCopy) {
        super(allocationType, toCopy);
    }
}
