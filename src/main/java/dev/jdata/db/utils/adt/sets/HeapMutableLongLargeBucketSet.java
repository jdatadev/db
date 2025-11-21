package dev.jdata.db.utils.adt.sets;

final class HeapMutableLongLargeBucketSet extends MutableLongLargeBucketSet {

    HeapMutableLongLargeBucketSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacityExponent, innerCapacityExponent);
    }
}
