package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntToLongWithRemoveNonBucketMapTest extends BaseMutableIntToLongNonContainsKeyNonBucketMapTest<MutableIntToLongWithRemoveNonBucketMap> {

    @Override
    MutableIntToLongWithRemoveNonBucketMap createMap(int initialCapacityExponent) {

        return new HeapMutableIntToLongWithRemoveNonBucketMap(AllocationType.HEAP, initialCapacityExponent);
    }

    @Override
    boolean remove(MutableIntToLongWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
