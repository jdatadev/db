package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableNonRemoveStaticMap<K, V> extends IMutableNonRemoveStaticMap<K, V> {

    public static <K, V> IHeapMutableNonRemoveStaticMap<K, V> create(int initialCapacity, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {

        return new HeapMutableObjectToObjectNonRemoveNonBucketMap<>(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity), createKeysArray,
                createValuesArray);
    }
}
