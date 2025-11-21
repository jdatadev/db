package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntToObjectDynamicMap<V> extends IMutableIntToObjectDynamicMap<V>, IHeapContainsMarker {

    public static <V> IHeapMutableIntToObjectDynamicMap<V> create(int initialCapacity, IntFunction<V[]> createValuesArray) {

        return HeapMutableIntToObjectMaxDistanceNonBucketMap.create(AllocationType.HEAP, initialCapacity, createValuesArray);
    }
}
