package dev.jdata.db.utils.adt.sets;

final class HeapMutableLongBucketSet extends MutableLongBucketSet implements IHeapMutableLongSet {

    static HeapMutableLongBucketSet create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongBucketSet::new);
    }

    private HeapMutableLongBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);
    }
}
