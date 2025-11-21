package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableLongArrayAllocator extends MutableLongArrayAllocator<ICachedMutableLongArray, CachedMutableLongArray> implements ICachedMutableLongArrayAllocator {

    @Override
    protected CachedMutableLongArray allocateMutableInstance(IntFunction<long[]> createElements, int minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return CachedMutableLongArray.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }

    @Override
    public ICachedMutableLongArray copyToMutable(ILongIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }
}
