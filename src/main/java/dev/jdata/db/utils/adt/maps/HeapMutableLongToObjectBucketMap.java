package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableLongToObjectBucketMap<V> extends MutableLongToObjectBucketMap<V> {

    static <V> HeapMutableLongToObjectBucketMap<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[][]> createOuterValuesArray,
            IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createOuterValuesArray, createValuesArray,
                HeapMutableLongToObjectBucketMap::new);
    }

    private HeapMutableLongToObjectBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[][]> createOuterValuesArray,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT, createOuterValuesArray,
                createValuesArray);
    }
}
