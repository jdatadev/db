package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableLongToIntWithRemoveNonBucketMapTest extends BaseMutableLongToIntNonContainsKeyNonBucketMapTest<MutableLongToIntWithRemoveNonBucketMap> {

    @Override
    MutableLongToIntWithRemoveNonBucketMap createMap(int initialCapacityExponent) {

        return new HeapMutableLongToIntWithRemoveNonBucketMap(AllocationType.HEAP, initialCapacityExponent);
    }

    @Override
    boolean remove(MutableLongToIntWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
