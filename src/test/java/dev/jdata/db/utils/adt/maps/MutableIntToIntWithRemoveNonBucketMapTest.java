package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntToIntWithRemoveNonBucketMapTest extends BaseMutableIntToIntNonContainsKeyNonBucketMapTest<MutableIntToIntWithRemoveNonBucketMap> {

    @Override
    MutableIntToIntWithRemoveNonBucketMap createMap(int initialCapacityExponent) {

        return new HeapMutableIntToIntWithRemoveNonBucketMap(AllocationType.HEAP, initialCapacityExponent);
    }

    @Override
    boolean remove(MutableIntToIntWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
