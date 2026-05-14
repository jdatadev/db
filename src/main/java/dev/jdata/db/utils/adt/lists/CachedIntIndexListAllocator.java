package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IntCapacityCachedElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedIntIndexListAllocator

        extends IntIndexListAllocator<ICachedIntIndexList, IHeapIntIndexList, ICachedMutableIntIndexList, CachedMutableIntIndexList, ICachedIntIndexListBuilder>
        implements ICachedIntIndexListAllocator {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

    CachedIntIndexListAllocator(MutableIntIndexListAllocator<?, ? extends MutableIntIndexList> mutableIntIndexListAllocator) {
        super(ALLOCATION_TYPE, new IntCapacityCachedElementsAllocators<>(ALLOCATION_TYPE, CapacityMax.INT,
                ICachedIntIndexList.class, CachedIntIndexList.class, (a, c) -> CachedIntIndexList.createEmptyValuesInitializable(a), l -> l.getElementsCapacity(),
                CachedIntIndexList::empty,
                CachedMutableIntIndexList.class, CachedMutableIntIndexList::create, l -> l.getElementsCapacity(),
                ICachedIntIndexListBuilder.class, CachedIntIndexListBuilder.class, (a, c) -> new CachedIntIndexListBuilder(a, c, mutableIntIndexListAllocator),
                b -> b.getCapacity()));
    }
}
