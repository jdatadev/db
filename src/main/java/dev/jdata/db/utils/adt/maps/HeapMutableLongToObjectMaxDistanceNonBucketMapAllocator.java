package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableLongToObjectMaxDistanceNonBucketMapAllocator<V>

        extends MutableLongToObjectMaxDistanceNonBucketMapAllocator<V, IHeapMutableLongToObjectDynamicMap<V>, HeapMutableLongToObjectMaxDistanceNonBucketMap<V>>
        implements IHeapMutableLongToObjectDynamicMapAllocator<V> {

    HeapMutableLongToObjectMaxDistanceNonBucketMapAllocator(IntFunction<V[]> createValuesArray) {
        super(createValuesArray);
    }

    @Override
    protected HeapMutableLongToObjectMaxDistanceNonBucketMap<V> allocateMutableInstance(IntFunction<V[]> createValues, int minimumCapacityExponent) {

        Objects.requireNonNull(createValues);
        Checks.isIntMinimumCapacityExponent(minimumCapacityExponent);

        return new HeapMutableLongToObjectMaxDistanceNonBucketMap<>(AllocationType.HEAP_ALLOCATOR, minimumCapacityExponent, createValues);
    }
}
