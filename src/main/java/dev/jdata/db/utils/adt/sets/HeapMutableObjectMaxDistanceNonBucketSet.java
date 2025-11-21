package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;

final class HeapMutableObjectMaxDistanceNonBucketSet<T> extends MutableObjectMaxDistanceNonBucketSet<T> implements IHeapMutableSet<T> {

    static <T> HeapMutableObjectMaxDistanceNonBucketSet<T> create(AllocationType allocationType, int initialCapacity, IntFunction<T[]> createHashArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createHashArray, HeapMutableObjectMaxDistanceNonBucketSet::new);
    }

    private HeapMutableObjectMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, IntFunction<T[]> createHashed) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createHashed);
    }
}
