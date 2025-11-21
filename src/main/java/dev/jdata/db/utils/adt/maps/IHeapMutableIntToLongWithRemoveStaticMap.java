package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntToLongWithRemoveStaticMap extends IMutableIntToLongWithRemoveStaticMap {

    public static IHeapMutableIntToLongWithRemoveStaticMap create(int initialCapacity) {

        return new HeapMutableIntToLongWithRemoveNonBucketMap(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity));
    }
}
