package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableLongToLongWithRemoveNonBucketMapTest extends BaseMutableLongToLongNonContainsKeyNonBucketMapTest<MutableLongToLongWithRemoveNonBucketMap> {

    @Override
    MutableLongToLongWithRemoveNonBucketMap createMap(int initialCapacity) {

        return HeapMutableLongToLongWithRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity);
    }

    @Override
    boolean remove(MutableLongToLongWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
