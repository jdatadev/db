package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;

final class HeapMutableIntMaxDistanceNonBucketSet extends MutableIntMaxDistanceNonBucketSet implements IHeapMutableIntSet {

    static HeapMutableIntMaxDistanceNonBucketSet create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableIntMaxDistanceNonBucketSet::new);
    }

    static HeapMutableIntMaxDistanceNonBucketSet copyToMutable(AllocationType allocationType, IIntIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.HEAP, mutableFrom);

        return new HeapMutableIntMaxDistanceNonBucketSet(allocationType, mutableFrom);
    }

    private HeapMutableIntMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }

    private HeapMutableIntMaxDistanceNonBucketSet(AllocationType allocationType, IIntIterableElementsView mutableFrom) {
        super(allocationType, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, mutableFrom);
    }
}
