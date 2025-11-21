package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongToLongWithRemoveStaticMap extends IMutableLongToLongWithRemoveStaticMap, IHeapContainsMarker {

    public static IHeapMutableLongToLongWithRemoveStaticMap create(int initialCapacity) {

        return new HeapMutableLongToLongWithRemoveNonBucketMap(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity));
    }
}
