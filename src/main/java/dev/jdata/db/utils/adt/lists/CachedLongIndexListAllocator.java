package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IntCapacityCachedElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;
import dev.jdata.db.utils.allocators.IAllocators;

final class CachedLongIndexListAllocator

        extends LongIndexListAllocator<ICachedLongIndexList, IHeapLongIndexList, ICachedMutableLongIndexList, CachedMutableLongIndexList, ICachedLongIndexListBuilder>
        implements ICachedLongIndexListAllocator, IAllocators {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

    CachedLongIndexListAllocator(MutableLongIndexListAllocator<?, ? extends MutableLongIndexList> mutableLongIndexListAllocator) {
        super(ALLOCATION_TYPE, new IntCapacityCachedElementsAllocators<>(ALLOCATION_TYPE, CapacityMax.INT,
                ICachedLongIndexList.class, CachedLongIndexList.class, (a, c) -> new CachedLongIndexList(a), l -> l.getElementsCapacity(), CachedLongIndexList::empty,
                CachedMutableLongIndexList.class, CachedMutableLongIndexList::new, l -> l.getElementsCapacity(),
                ICachedLongIndexListBuilder.class, CachedLongIndexListBuilder.class, (a, c) -> new CachedLongIndexListBuilder(a, mutableLongIndexListAllocator),
                b -> b.getIntCapacity()));
    }
}
