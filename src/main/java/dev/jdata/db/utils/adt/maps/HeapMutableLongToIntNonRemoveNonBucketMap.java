package dev.jdata.db.utils.adt.maps;

final class HeapMutableLongToIntNonRemoveNonBucketMap

        extends MutableLongToIntNonRemoveNonBucketMap<HeapMutableLongToIntNonRemoveNonBucketMap>
        implements IHeapMutableLongToIntNonRemoveStaticMap {

    static HeapMutableLongToIntNonRemoveNonBucketMap create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongToIntNonRemoveNonBucketMap::new);
    }

    private HeapMutableLongToIntNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }
}
