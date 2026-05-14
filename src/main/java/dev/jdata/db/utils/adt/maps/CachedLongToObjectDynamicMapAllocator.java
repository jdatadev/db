package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IntCapacityCachedElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.classes.Classes;

final class CachedLongToObjectDynamicMapAllocator<V>

        extends LongToObjectDynamicMapAllocator<

                        V,
                        ICachedLongToObjectDynamicMap<V>,
                        IHeapLongToObjectDynamicMap<V>,
                        ICachedMutableLongToObjectDynamicMap<V>,
                        CachedMutableLongToObjectMaxDistanceNonBucketMap<V>,
                        ICachedLongToObjectDynamicMapBuilder<V>>

        implements ICachedLongToObjectDynamicMapAllocator<V> {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

    CachedLongToObjectDynamicMapAllocator(CachedMutableLongToObjectDynamicMapAllocator<V> mutableLongToObjectDynamicMapAllocator, IntFunction<V[]> createValuesArray) {
        super(ALLOCATION_TYPE, new IntCapacityCachedElementsAllocators<>(ALLOCATION_TYPE, CapacityMax.INT,

                Classes.genericClass(ICachedLongToObjectDynamicMap.class), Classes.genericClass(CachedLongToObjectMaxDistanceNonBucketMap.class),
                (a, c) -> CachedLongToObjectMaxDistanceNonBucketMap.createEmptyValuesInitializable(a), m -> m.getElementsCapacity(),
                CachedLongToObjectMaxDistanceNonBucketMap::empty,
                Classes.genericClass(CachedMutableLongToObjectMaxDistanceNonBucketMap.class),
                (a, c) -> CachedMutableLongToObjectMaxDistanceNonBucketMap.create(a, c, createValuesArray), m -> m.getElementsCapacity(),
                Classes.genericClass(ICachedLongToObjectDynamicMapBuilder.class),
                CachedLongToObjectDynamicMapBuilder.class, (a, c) -> new CachedLongToObjectDynamicMapBuilder<>(a, c, mutableLongToObjectDynamicMapAllocator, createValuesArray),
                b -> b.getCapacity()));
    }
}
