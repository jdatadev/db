package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

final class HeapMutableIntMaxDistanceNonBucketSet extends MutableIntMaxDistanceNonBucketSet implements IHeapMutableIntSet {

    HeapMutableIntMaxDistanceNonBucketSet(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    HeapMutableIntMaxDistanceNonBucketSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor);
    }

    HeapMutableIntMaxDistanceNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    @Override
    public IHeapIntSet copyToImmutable(IHeapIntSetAllocator immutableAllocator) {

        Objects.requireNonNull(immutableAllocator);

        throw new UnsupportedOperationException();
    }
}
