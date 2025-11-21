package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongToIntWithRemoveStaticMap extends IMutableLongToIntWithRemoveStaticMap {

    public static IHeapMutableLongToIntWithRemoveStaticMap create(int initialCapacity) {

        return HeapMutableLongToIntWithRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity);
    }
}
