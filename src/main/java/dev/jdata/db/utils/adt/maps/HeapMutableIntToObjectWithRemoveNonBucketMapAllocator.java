package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableIntToObjectWithRemoveNonBucketMapAllocator<V>

        extends MutableIntToObjectWithRemoveNonBucketMapAllocator<V, IHeapMutableIntToObjectWithRemoveStaticMap<V>, HeapMutableIntToObjectWithRemoveNonBucketMap<V>>
        implements IHeapMutableIntToObjectWithRemoveStaticMapAllocator<V> {

    HeapMutableIntToObjectWithRemoveNonBucketMapAllocator(IntFunction<V[]> createArray) {
        super(createArray);
    }

    @Override
    protected HeapMutableIntToObjectWithRemoveNonBucketMap<V> allocateMutableInstance(IntFunction<V[]> createElements, int minimumCapacityExponent) {

        Objects.requireNonNull(createElements);
        Checks.isIntMinimumCapacityExponent(minimumCapacityExponent);

        return new HeapMutableIntToObjectWithRemoveNonBucketMap<>(AllocationType.HEAP_ALLOCATOR, minimumCapacityExponent, createElements);
    }
}
