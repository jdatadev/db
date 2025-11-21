package dev.jdata.db.utils.adt.arrays;

final class CachedMutableLongLargeArray extends MutableLongLargeArray implements ICachedMutableLongLargeArray {

    static CachedMutableLongLargeArray create(AllocationType allocationType, long minimumCapacity) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.CACHE, minimumCapacity, CachedMutableLongLargeArray::new);
    }

    private static CachedMutableLongLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new CachedMutableLongLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    private CachedMutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }
}
