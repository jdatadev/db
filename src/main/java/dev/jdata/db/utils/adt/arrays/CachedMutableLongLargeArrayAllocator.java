package dev.jdata.db.utils.adt.arrays;

import java.util.function.LongFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class CachedMutableLongLargeArrayAllocator

        extends MutableLongLargeArrayAllocator<ICachedMutableLongLargeArray, CachedMutableLongLargeArray>
        implements ICachedMutableLongLargeArrayAllocator {

    @Override
    protected CachedMutableLongLargeArray allocateMutableInstance(LongFunction<long[]> createElements, long minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return CachedMutableLongLargeArray.create(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }
}
