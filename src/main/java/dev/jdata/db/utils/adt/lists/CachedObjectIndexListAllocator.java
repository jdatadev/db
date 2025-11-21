package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IntCapacityCachedElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.classes.Classes;

final class CachedObjectIndexListAllocator<T>

        extends ObjectIndexListAllocator<T, ICachedIndexList<T>, IHeapIndexList<T>, ICachedMutableIndexList<T>, CachedMutableObjectIndexList<T>, ICachedIndexListBuilder<T>>
        implements ICachedIndexListAllocator<T>, IAllocators {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

    CachedObjectIndexListAllocator(IntFunction<T[]> createElementsArray,
            MutableObjectIndexListAllocator<T, ?, ? extends MutableObjectIndexList<T>> mutableObjectIndexListAllocator) {
        super(ALLOCATION_TYPE, new IntCapacityCachedElementsAllocators<>(ALLOCATION_TYPE, CapacityMax.INT,
                Classes.genericClass(ICachedIndexList.class), Classes.genericClass(CachedObjectIndexList.class),
                (a, c) -> new CachedObjectIndexList<>(a), l -> l.getElementsCapacity(), CachedObjectIndexList::empty,
                Classes.genericClass(CachedMutableObjectIndexList.class),
                (a, c) -> new CachedMutableObjectIndexList<>(a, createElementsArray, c), l -> l.getElementsCapacity(),
                Classes.genericClass(ICachedIndexListBuilder.class), Classes.genericClass(CachedObjectIndexListBuilder.class),
                (a, c) -> new CachedObjectIndexListBuilder<>(a, c, mutableObjectIndexListAllocator), b -> b.getIntCapacity()));
    }
}
