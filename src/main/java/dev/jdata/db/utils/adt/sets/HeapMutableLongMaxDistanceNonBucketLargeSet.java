package dev.jdata.db.utils.adt.sets;

final class HeapMutableLongMaxDistanceNonBucketLargeSet extends MutableLongMaxDistanceNonBucketLargeSet implements IHeapMutableLongLargeSet {

    static HeapMutableLongMaxDistanceNonBucketLargeSet create(AllocationType allocationType, long initialCapacity) {

        return instantiateWithOuterExponentInnerExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongMaxDistanceNonBucketLargeSet::new);
    }

    private HeapMutableLongMaxDistanceNonBucketLargeSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, DEFAULT_LOAD_FACTOR);
    }
}
