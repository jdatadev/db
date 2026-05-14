package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IntCapacityCachedElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedLongIndexListAllocator

        extends LongIndexListAllocator<ICachedLongIndexList, IHeapLongIndexList, ICachedMutableLongIndexList, CachedMutableLongIndexList, ICachedLongIndexListBuilder>
        implements ICachedLongIndexListAllocator {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

    CachedLongIndexListAllocator(MutableLongIndexListAllocator<?, ? extends MutableLongIndexList> mutableLongIndexListAllocator) {
        super(ALLOCATION_TYPE, new IntCapacityCachedElementsAllocators<>(ALLOCATION_TYPE, CapacityMax.INT,
                ICachedLongIndexList.class, CachedLongIndexList.class, (a, c) -> CachedLongIndexList.createEmptyValuesInitializable(a), l -> l.getElementsCapacity(),
                CachedLongIndexList::empty,
                CachedMutableLongIndexList.class, CachedMutableLongIndexList::create, l -> l.getElementsCapacity(),
                ICachedLongIndexListBuilder.class, CachedLongIndexListBuilder.class, (a, c) -> new CachedLongIndexListBuilder(a, c, mutableLongIndexListAllocator),
                b -> b.getCapacity()));
    }
}
