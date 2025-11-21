package dev.jdata.db.utils.adt.maps;

final class HeapMutableLongToLongWithRemoveNonBucketMap extends MutableLongToLongWithRemoveNonBucketMap implements IHeapMutableLongToLongWithRemoveStaticMap {

    static HeapMutableLongToLongWithRemoveNonBucketMap create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongToLongWithRemoveNonBucketMap::new);
    }

    private HeapMutableLongToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }
}
