package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableObjectIndexListAllocator<T>

        extends MutableObjectIndexListAllocator<T, ICachedMutableIndexList<T>, MutableObjectIndexList<T>>
        implements ICachedMutableIndexListAllocator<T> {

    CachedMutableObjectIndexListAllocator(IntFunction<T[]> createElementsArray) {
        super(createElementsArray);
    }

    @Override
    protected MutableObjectIndexList<T> allocateMutableInstance(IntFunction<T[]> createElements, int minimumCapacity) {

        return new CachedMutableObjectIndexList<>(AllocationType.CACHING_ALLOCATOR, createElements, minimumCapacity);
    }
}
