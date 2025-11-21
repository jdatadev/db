package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;

final class HeapMutableLongMaxDistanceNonBucketSet extends MutableLongMaxDistanceNonBucketSet implements IHeapMutableLongSet {

    static HeapMutableLongMaxDistanceNonBucketSet create(AllocationType allocationType) {

        checkMutableCreateParameters(allocationType, AllocationMechanism.HEAP);

        return new HeapMutableLongMaxDistanceNonBucketSet(allocationType);
    }

    static HeapMutableLongMaxDistanceNonBucketSet create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongMaxDistanceNonBucketSet::new);
    }

    static HeapMutableLongMaxDistanceNonBucketSet copyToMutable(AllocationType allocationType, ILongIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.HEAP, mutableFrom);

        return new HeapMutableLongMaxDistanceNonBucketSet(allocationType, mutableFrom);
    }

    private HeapMutableLongMaxDistanceNonBucketSet(AllocationType allocationType) {
        super(allocationType, DEFAULT_INITIAL_CAPACITY_EXPONENT, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }

    private HeapMutableLongMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }

    private HeapMutableLongMaxDistanceNonBucketSet(AllocationType allocationType, ILongIterableElementsView mutableFrom) {
        super(allocationType, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, mutableFrom);
    }
}
