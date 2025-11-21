package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableIntIndexListAllocator

        extends MutableIntIndexListAllocator<ICachedMutableIntIndexList, CachedMutableIntIndexList>
        implements ICachedMutableIntIndexListAllocator {

    @Override
    protected CachedMutableIntIndexList allocateMutableInstance(IntFunction<int[]> createElements, int minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return CachedMutableIntIndexList.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }

    @Override
    public ICachedMutableIntIndexList copyToMutable(IIntIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        return CachedMutableIntIndexList.copyToMutable(AllocationType.CACHING_ALLOCATOR, mutableFrom);
    }
}
