package dev.jdata.db.utils.adt.maps;

final class HeapMutableLongToIntBucketMap extends MutableLongToIntBucketMap {

    static HeapMutableLongToIntBucketMap create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongToIntBucketMap::new);
    }

    private HeapMutableLongToIntBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);
    }
}
