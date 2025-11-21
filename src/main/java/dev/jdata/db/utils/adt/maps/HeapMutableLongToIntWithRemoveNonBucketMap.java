package dev.jdata.db.utils.adt.maps;

final class HeapMutableLongToIntWithRemoveNonBucketMap extends MutableLongToIntWithRemoveNonBucketMap implements IHeapMutableLongToIntWithRemoveStaticMap {

    HeapMutableLongToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
