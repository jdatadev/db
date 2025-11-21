package dev.jdata.db.utils.adt.maps;

final class HeapMutableIntToIntNonRemoveNonBucketMap extends MutableIntToIntNonRemoveNonBucketMap implements IHeapMutableIntToIntNonRemoveStaticMap {

    HeapMutableIntToIntNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
