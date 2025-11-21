package dev.jdata.db.utils.adt.sets;

final class HeapMutableLongBucketSet extends MutableLongBucketSet implements IHeapMutableLongSet {

    HeapMutableLongBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
