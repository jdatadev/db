package dev.jdata.db.utils.adt.arrays;

final class CachedMutableLongLargeArray extends MutableLongLargeArray implements ICachedMutableLongLargeArray {

    CachedMutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);

        AllocationType.checkIsCached(allocationType);
    }
}
