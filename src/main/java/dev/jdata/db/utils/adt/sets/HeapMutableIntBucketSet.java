package dev.jdata.db.utils.adt.sets;

final class HeapMutableIntBucketSet extends MutableIntBucketSet implements IHeapMutableIntSet {

    static HeapMutableIntBucketSet create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableIntBucketSet::new);
    }

    private HeapMutableIntBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);
    }
}
