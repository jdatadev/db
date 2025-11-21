package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntToIntNonRemoveStaticMap extends IMutableIntToIntNonRemoveStaticMap {

    public static IHeapMutableIntToIntNonRemoveStaticMap create(int initialCapacity) {

        return HeapMutableIntToIntNonRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity);
    }
}
