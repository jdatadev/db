package dev.jdata.db.utils.adt.maps;

final class HeapMutableIntToIntNonRemoveNonBucketMap extends MutableIntToIntNonRemoveNonBucketMap implements IHeapMutableIntToIntNonRemoveStaticMap {

    static <K, V> HeapMutableIntToIntNonRemoveNonBucketMap create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableIntToIntNonRemoveNonBucketMap::new);
    }

    private HeapMutableIntToIntNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }
}
