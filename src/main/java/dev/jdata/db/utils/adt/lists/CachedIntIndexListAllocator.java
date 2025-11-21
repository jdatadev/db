package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IntCapacityCachedElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;
import dev.jdata.db.utils.allocators.IAllocators;

final class CachedIntIndexListAllocator

        extends IntIndexListAllocator<ICachedIntIndexList, IHeapIntIndexList, ICachedMutableIntIndexList, CachedMutableIntIndexList, ICachedIntIndexListBuilder>
        implements IAllocators {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

    CachedIntIndexListAllocator(AllocationType allocationType, MutableIntIndexListAllocator<?, ? extends MutableIntIndexList> mutableIntIndexListAllocator) {
        super(ALLOCATION_TYPE, new IntCapacityCachedElementsAllocators<>(ALLOCATION_TYPE, CapacityMax.INT,
                ICachedIntIndexList.class, CachedIntIndexList.class, (a, c) -> new CachedIntIndexList(a), l -> l.getElementsCapacity(), CachedIntIndexList::empty,
                CachedMutableIntIndexList.class, CachedMutableIntIndexList::new, l -> l.getElementsCapacity(),
                ICachedIntIndexListBuilder.class, CachedIntIndexListBuilder.class, (a, c) -> new CachedIntIndexListBuilder(a, mutableIntIndexListAllocator),
                b -> b.getIntCapacity()));
    }
}
