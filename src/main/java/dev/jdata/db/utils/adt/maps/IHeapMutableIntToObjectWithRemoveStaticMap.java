package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntToObjectWithRemoveStaticMap<V> extends IMutableIntToObjectWithRemoveStaticMap<V>, IHeapContainsMarker {

    public static <V> IHeapMutableIntToObjectWithRemoveStaticMap<V> create(int initialCapacity, IntFunction<V[]> createValuesArray) {

        return HeapMutableIntToObjectWithRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity, createValuesArray);
    }
}
