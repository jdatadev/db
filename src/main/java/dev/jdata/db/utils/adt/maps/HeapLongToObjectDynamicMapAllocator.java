package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IntCapacityHeapElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapLongToObjectDynamicMapAllocator<V>

        extends LongToObjectDynamicMapAllocator<

                        V,
                        IHeapLongToObjectDynamicMap<V>,
                        IHeapLongToObjectDynamicMap<V>,
                        IHeapMutableLongToObjectDynamicMap<V>,
                        HeapMutableLongToObjectMaxDistanceNonBucketMap<V>,
                        IHeapLongToObjectDynamicMapBuilder<V>>

        implements IHeapLongToObjectDynamicMapAllocator<V> {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

    HeapLongToObjectDynamicMapAllocator(IntFunction<V[]> createValuesArray) {
        super(ALLOCATION_TYPE, new IntCapacityHeapElementsAllocators<>(ALLOCATION_TYPE, HeapLongToObjectMaxDistanceNonBucketMap::empty,
                (a, c) -> HeapMutableLongToObjectMaxDistanceNonBucketMap.create(a, c, createValuesArray),
                (a, c) -> HeapLongToObjectDynamicMapBuilder.create(a, c, createValuesArray)));
    }
}
