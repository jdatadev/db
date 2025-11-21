package dev.jdata.db.utils.adt.maps;

final class HeapMutableLongToIntWithRemoveNonBucketMap

        extends MutableLongToIntWithRemoveNonBucketMap<HeapMutableLongToIntWithRemoveNonBucketMap>
        implements IHeapMutableLongToIntWithRemoveStaticMap {

    static HeapMutableLongToIntWithRemoveNonBucketMap create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongToIntWithRemoveNonBucketMap::new);
    }

    private HeapMutableLongToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }
}
