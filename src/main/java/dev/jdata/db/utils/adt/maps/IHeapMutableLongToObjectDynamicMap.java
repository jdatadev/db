package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongToObjectDynamicMap<V> extends IMutableLongToObjectDynamicMap<V> {

    public static <V> IHeapMutableLongToObjectDynamicMap<V> create(int initialCapacity, IntFunction<V[]> createValuesArray) {

        KeyMapChecks.checkCreateParameters(initialCapacity, createValuesArray);

        return new HeapMutableLongToObjectMaxDistanceNonBucketMap<>(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity), createValuesArray);
    }

    public static <V> IHeapMutableLongToObjectDynamicMap<V> create(int initialCapacity, int capacityExponentIncrease, float loadFactor,
            IntFunction<V[]> createValuesArray) {

        KeyMapChecks.checkCreateParameters(initialCapacity, capacityExponentIncrease, loadFactor, createValuesArray);

        return new HeapMutableLongToObjectMaxDistanceNonBucketMap<>(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity), capacityExponentIncrease, loadFactor,
                createValuesArray);
    }
}
