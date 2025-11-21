package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableDynamicMap<K, V> extends IMutableDynamicMap<K, V> {

    public static <K, V> IHeapMutableDynamicMap<K, V> create(int initialCapacity, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {

        KeyMapChecks.checkCreateParameters(initialCapacity, createKeysArray, createValuesArray);

        return new HeapMutableObjectMaxDistanceNonBucketMap<>(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity), createKeysArray,
                createValuesArray);
    }

    public static <K, V> IHeapMutableDynamicMap<K, V> create(int initialCapacity, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {

        KeyMapChecks.checkCreateParameters(initialCapacity, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);

        return new HeapMutableObjectMaxDistanceNonBucketMap<>(AllocationType.HEAP, initialCapacity, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);
    }
}
