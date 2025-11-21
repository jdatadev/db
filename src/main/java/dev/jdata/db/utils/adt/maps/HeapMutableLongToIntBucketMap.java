package dev.jdata.db.utils.adt.maps;

final class HeapMutableLongToIntBucketMap extends MutableLongToIntBucketMap {

    HeapMutableLongToIntBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
