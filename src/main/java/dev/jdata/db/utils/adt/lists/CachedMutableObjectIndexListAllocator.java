package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableObjectIndexListAllocator<T>

        extends MutableObjectIndexListAllocator<T, ICachedMutableIndexList<T>, MutableObjectIndexList<T>>
        implements ICachedMutableIndexListAllocator<T> {

    @Deprecated // pass as parameter to copyToMutable() ?
    private final IntFunction<T[]> createElementsArray;

    CachedMutableObjectIndexListAllocator(IntFunction<T[]> createElementsArray) {
        super(createElementsArray);

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
    }

    @Override
    protected MutableObjectIndexList<T> allocateMutableInstance(IntFunction<T[]> createElements, int minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return CachedMutableObjectIndexList.create(AllocationType.CACHING_ALLOCATOR, createElements, minimumCapacity);
    }

    @Override
    public ICachedMutableIndexList<T> copyToMutable(IObjectIterableElementsView<T> mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        return CachedMutableObjectIndexList.copyToMutable(AllocationType.CACHING_ALLOCATOR, mutableFrom, createElementsArray);
    }
}
