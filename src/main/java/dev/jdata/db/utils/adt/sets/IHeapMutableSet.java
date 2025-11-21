package dev.jdata.db.utils.adt.sets;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableSet<T> extends IMutableSet<T> {

    public static <T> IHeapMutableSet<T> create(int initialCapacity, IntFunction<T[]> createHashed) {

        Checks.isIntInitialCapacity(initialCapacity);
        Objects.requireNonNull(createHashed);

        return new HeapMutableObjectMaxDistanceNonBucketSet<>(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity), createHashed);
    }
}
