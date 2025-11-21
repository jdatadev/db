package dev.jdata.db.utils.adt.sets;

final class HeapMutableIntBucketSet extends MutableIntBucketSet implements IHeapMutableIntSet {

    HeapMutableIntBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
