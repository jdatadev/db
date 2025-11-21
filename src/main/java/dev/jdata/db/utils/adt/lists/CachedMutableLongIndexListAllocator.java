package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableLongIndexListAllocator

        extends MutableLongIndexListAllocator<ICachedMutableLongIndexList, MutableLongIndexList>
        implements ICachedMutableLongIndexListAllocator {

    @Override
    protected MutableLongIndexList allocateMutableInstance(IntFunction<long[]> createElements, int minimumCapacity) {

        Objects.requireNonNull(createElements);
        Checks.isIntMinimumCapacity(minimumCapacity);

        return new CachedMutableLongIndexList(AllocationType.CACHING_ALLOCATOR, minimumCapacity);
    }
}
