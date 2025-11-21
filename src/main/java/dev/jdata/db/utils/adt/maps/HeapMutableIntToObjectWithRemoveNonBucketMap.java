package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapMutableIntToObjectWithRemoveNonBucketMap<V>

        extends MutableIntToObjectWithRemoveNonBucketMap<V, HeapMutableIntToObjectWithRemoveNonBucketMap<V>>
        implements IHeapMutableIntToObjectWithRemoveStaticMap<V> {

    static <V> HeapMutableIntToObjectWithRemoveNonBucketMap<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createValuesArray, HeapMutableIntToObjectWithRemoveNonBucketMap::new);
    }

    private HeapMutableIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }
}
