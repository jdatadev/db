package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntToLongWithRemoveStaticMap extends IMutableIntToLongWithRemoveStaticMap {

    public static IHeapMutableIntToLongWithRemoveStaticMap create(int initialCapacity) {

        return HeapMutableIntToLongWithRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity);
    }
}
