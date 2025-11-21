package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableLongToIntWithRemoveNonBucketMapTest extends BaseMutableLongToIntNonContainsKeyNonBucketMapTest<HeapMutableLongToIntWithRemoveNonBucketMap> {

    @Override
    HeapMutableLongToIntWithRemoveNonBucketMap createMap(int initialCapacity) {

        return HeapMutableLongToIntWithRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity);
    }

    @Override
    boolean remove(HeapMutableLongToIntWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
