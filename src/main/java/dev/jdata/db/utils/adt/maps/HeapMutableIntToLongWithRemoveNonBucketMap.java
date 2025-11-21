package dev.jdata.db.utils.adt.maps;

final class HeapMutableIntToLongWithRemoveNonBucketMap extends MutableIntToLongWithRemoveNonBucketMap implements IHeapMutableIntToLongWithRemoveStaticMap {

    HeapMutableIntToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }

    HeapMutableIntToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    HeapMutableIntToLongWithRemoveNonBucketMap(AllocationType allocationType, MutableIntToLongWithRemoveNonBucketMap toCopy) {
        super(allocationType, toCopy);
    }
}
