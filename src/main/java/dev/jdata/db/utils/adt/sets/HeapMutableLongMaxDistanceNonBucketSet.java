package dev.jdata.db.utils.adt.sets;

final class HeapMutableLongMaxDistanceNonBucketSet extends MutableLongMaxDistanceNonBucketSet implements IHeapMutableLongSet {

    HeapMutableLongMaxDistanceNonBucketSet(AllocationType allocationType) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY_EXPONENT);
    }

    HeapMutableLongMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        this(allocationType, initialCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    HeapMutableLongMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, float loadFactor) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor);
    }

    HeapMutableLongMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }
}
