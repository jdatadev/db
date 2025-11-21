package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableDynamicMap<K, V> extends IMutableDynamicMap<K, V> {

    public static <K, V> IHeapMutableDynamicMap<K, V> create(int initialCapacity, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {

        return HeapMutableObjectToObjectMaxDistanceNonBucketMap.create(AllocationType.HEAP, initialCapacity, createKeysArray, createValuesArray);
    }
}
