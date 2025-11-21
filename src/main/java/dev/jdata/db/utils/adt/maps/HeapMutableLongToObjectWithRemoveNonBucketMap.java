package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableLongToObjectWithRemoveNonBucketMap<V> extends MutableLongToObjectWithRemoveNonBucketMap<V> implements IHeapMutableLongToObjectWithRemoveStaticMap<V> {

    static <V> HeapMutableLongToObjectWithRemoveNonBucketMap<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createValuesArray, HeapMutableLongToObjectWithRemoveNonBucketMap::new);
    }

    private HeapMutableLongToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }
}
