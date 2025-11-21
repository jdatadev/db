package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableIntToObjectDynamicMap<V> extends IMutableIntToObjectDynamicMap<V>, IHeapContainsMarker {

    public static <V> IHeapMutableIntToObjectDynamicMap<V> create(int initialCapacity, IntFunction<V[]> createValuesArray) {

        Checks.isIntInitialCapacity(initialCapacity);
        Objects.requireNonNull(createValuesArray);

        return new HeapMutableIntToObjectMaxDistanceNonBucketMap<>(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity), createValuesArray);
    }
}
