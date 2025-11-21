package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableLongToLongWithRemoveNonBucketMapTest extends BaseMutableLongToLongNonContainsKeyNonBucketMapTest<MutableLongToLongWithRemoveNonBucketMap> {

    @Override
    MutableLongToLongWithRemoveNonBucketMap createMap(int initialCapacityExponent) {

        return new HeapMutableLongToLongWithRemoveNonBucketMap(AllocationType.HEAP, initialCapacityExponent);
    }

    @Override
    boolean remove(MutableLongToLongWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
