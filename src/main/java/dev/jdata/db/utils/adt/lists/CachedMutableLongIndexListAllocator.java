package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableLongIndexListAllocator

        extends MutableLongIndexListAllocator<ICachedMutableLongIndexList, MutableLongIndexList>
        implements ICachedMutableLongIndexListAllocator {

    @Override
    protected MutableLongIndexList allocateMutableInstance(IntFunction<long[]> createElements, int minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return CachedMutableLongIndexList.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }

    @Override
    public ICachedMutableLongIndexList copyToMutable(ILongIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        return CachedMutableLongIndexList.copyToMutable(AllocationType.CACHING_ALLOCATOR, mutableFrom);
    }
}
