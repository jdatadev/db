package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class HeapLongToObjectMaxDistanceNonBucketMap<V>

        extends LongToObjectMaxDistanceNonBucketMap<V, HeapLongToObjectMaxDistanceNonBucketMap<V>>
        implements IHeapLongToObjectDynamicMap<V> {

    static <V> IHeapLongToObjectDynamicMap<V> empty() {

        return HeapLongToObjectEmptyDynamicMap.empty();
    }

    static <V> HeapLongToObjectMaxDistanceNonBucketMap<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, createValuesArray,
                (a, e, c) -> new HeapLongToObjectMaxDistanceNonBucketMap<>(a, e, c));
    }

    static <V> HeapLongToObjectMaxDistanceNonBucketMap<V> withMakeElementsFrom(AllocationType allocationType, MutableLongToObjectMaxDistanceNonBucketMap<V, ?> toInitializeFrom) {

        checkWithMakeElementsFrom(allocationType, AllocationMechanism.HEAP, toInitializeFrom);

        return new HeapLongToObjectMaxDistanceNonBucketMap<>(allocationType, toInitializeFrom, null);
    }

    private HeapLongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    private HeapLongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, BaseLongToObjectMaxDistanceNonBucketMap<V, ?> toInitializeFrom, Void disambiguate) {
        super(allocationType, toInitializeFrom, disambiguate);
    }
}
