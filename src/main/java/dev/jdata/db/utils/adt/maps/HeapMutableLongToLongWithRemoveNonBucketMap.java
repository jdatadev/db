package dev.jdata.db.utils.adt.maps;

final class HeapMutableLongToLongWithRemoveNonBucketMap extends MutableLongToLongWithRemoveNonBucketMap implements IHeapMutableLongToLongWithRemoveStaticMap {

    HeapMutableLongToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
