package dev.jdata.db.utils.adt.maps;

final class HeapMutableIntToIntMaxDistanceNonBucketMap extends MutableIntToIntMaxDistanceNonBucketMap {

    static HeapMutableIntToIntMaxDistanceNonBucketMap create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, null,
                (a, e, c) -> new HeapMutableIntToIntMaxDistanceNonBucketMap(a, e));
    }

    private HeapMutableIntToIntMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }
}
