package dev.jdata.db.utils.adt.sets;

final class HeapMutableLongLargeMaxDistanceNonBucketSet extends MutableLongLargeMaxDistanceNonBucketSet implements IHeapMutableLongLargeSet {

    private HeapMutableLongLargeMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
