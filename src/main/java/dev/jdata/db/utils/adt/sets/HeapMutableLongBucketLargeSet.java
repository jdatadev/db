package dev.jdata.db.utils.adt.sets;

final class HeapMutableLongBucketLargeSet extends MutableLongBucketLargeSet {

    static HeapMutableLongBucketLargeSet create(AllocationType allocationType, long initialCapacity) {

        return instantiateWithOuterExponentInnerExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongBucketLargeSet::new);
    }

    private HeapMutableLongBucketLargeSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacityExponent, innerCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }
}
