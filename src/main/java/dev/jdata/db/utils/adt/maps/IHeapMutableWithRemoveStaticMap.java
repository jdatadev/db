package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableWithRemoveStaticMap<K, V> extends IMutableWithRemoveStaticMap<K, V> {

    public static <K, V> IHeapMutableWithRemoveStaticMap<K, V> create(int initialCapacity, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {

        return new HeapMutableObjectWithRemoveNonBucketMap<>(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity), createKeysArray,
                createValuesArray);
    }
}
