package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;

final class HeapMutableObjectMaxDistanceNonBucketSet<T> extends MutableObjectMaxDistanceNonBucketSet<T> implements IHeapMutableSet<T> {

    HeapMutableObjectMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, IntFunction<T[]> createHashed) {
        super(allocationType, initialCapacityExponent, createHashed);
    }

    HeapMutableObjectMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<T[]> createHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed);
    }
}
