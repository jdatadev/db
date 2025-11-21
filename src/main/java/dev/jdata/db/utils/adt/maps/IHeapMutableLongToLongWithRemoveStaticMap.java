package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongToLongWithRemoveStaticMap extends IMutableLongToLongWithRemoveStaticMap, IHeapContainsMarker {

    public static IHeapMutableLongToLongWithRemoveStaticMap create(int initialCapacity) {

        return HeapMutableLongToLongWithRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity);
    }
}
