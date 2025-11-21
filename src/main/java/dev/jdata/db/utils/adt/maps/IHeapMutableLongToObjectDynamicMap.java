package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongToObjectDynamicMap<V> extends IMutableLongToObjectDynamicMap<V> {

    public static <V> IHeapMutableLongToObjectDynamicMap<V> create(int initialCapacity, IntFunction<V[]> createValuesArray) {

        return HeapMutableLongToObjectMaxDistanceNonBucketMap.create(AllocationType.HEAP, initialCapacity, createValuesArray);
    }
}
