package dev.jdata.db.utils.adt.maps;

final class HeapMutableIntToLongWithRemoveNonBucketMap extends MutableIntToLongWithRemoveNonBucketMap implements IHeapMutableIntToLongWithRemoveStaticMap {

    static HeapMutableIntToLongWithRemoveNonBucketMap create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableIntToLongWithRemoveNonBucketMap::new);
    }

    private HeapMutableIntToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }
}
