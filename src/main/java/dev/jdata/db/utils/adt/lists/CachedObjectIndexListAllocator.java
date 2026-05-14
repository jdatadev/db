package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IntCapacityCachedElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.classes.Classes;

final class CachedObjectIndexListAllocator<T>

        extends ObjectIndexListAllocator<T, ICachedIndexList<T>, IHeapIndexList<T>, ICachedMutableIndexList<T>, CachedMutableObjectIndexList<T>, ICachedIndexListBuilder<T>>
        implements ICachedIndexListAllocator<T> {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

    CachedObjectIndexListAllocator(MutableObjectIndexListAllocator<T, ?, ? extends MutableObjectIndexList<T>> mutableObjectIndexListAllocator,
            IntFunction<T[]> createElementsArray) {
        super(ALLOCATION_TYPE, new IntCapacityCachedElementsAllocators<>(ALLOCATION_TYPE, CapacityMax.INT,
                Classes.genericClass(ICachedIndexList.class), Classes.genericClass(CachedObjectIndexList.class),
                (a, c) -> CachedObjectIndexList.createEmptyValuesInitializable(a), l -> l.getElementsCapacity(), CachedObjectIndexList::empty,
                Classes.genericClass(CachedMutableObjectIndexList.class),
                (a, c) -> CachedMutableObjectIndexList.create(a, createElementsArray, c), l -> l.getElementsCapacity(),
                Classes.genericClass(ICachedIndexListBuilder.class), Classes.genericClass(CachedObjectIndexListBuilder.class),
                (a, c) -> new CachedObjectIndexListBuilder<>(a, c, mutableObjectIndexListAllocator, createElementsArray), b -> b.getCapacity()));
    }
}
