package dev.jdata.db.utils.adt.maps;

final class HeapMutableIntToIntWithRemoveNonBucketMap extends MutableIntToIntWithRemoveNonBucketMap {

    static HeapMutableIntToIntWithRemoveNonBucketMap create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableIntToIntWithRemoveNonBucketMap::new);
    }

    private HeapMutableIntToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }
}
