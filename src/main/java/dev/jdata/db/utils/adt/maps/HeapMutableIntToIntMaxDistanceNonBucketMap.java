package dev.jdata.db.utils.adt.maps;

final class HeapMutableIntToIntMaxDistanceNonBucketMap extends MutableIntToIntMaxDistanceNonBucketMap {

    HeapMutableIntToIntMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
