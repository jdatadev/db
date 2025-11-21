package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableIntIndexListAllocator

        extends MutableIntIndexListAllocator<ICachedMutableIntIndexList, CachedMutableIntIndexList>
        implements ICachedMutableIntIndexListAllocator {

    @Override
    protected CachedMutableIntIndexList allocateMutableInstance(IntFunction<int[]> createElements, int minimumCapacity) {

        Objects.requireNonNull(createElements);
        Checks.isIntMinimumCapacity(minimumCapacity);

        return new CachedMutableIntIndexList(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }
}
