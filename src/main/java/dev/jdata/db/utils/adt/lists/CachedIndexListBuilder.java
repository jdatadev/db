package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.ICacheable;

final class CachedIndexListBuilder<T> extends IndexListBuilder<T, CachedIndexList<T>, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR> implements ICacheable {

    private final CacheIndexListAllocator<T> listAllocator;

    private CachedIndexListBuilder(AllocationType allocationType, int initialCapacity, CacheIndexListAllocator<T> listAllocator) {
        super(allocationType, initialCapacity, listAllocator);

        this.listAllocator = Objects.requireNonNull(listAllocator);
    }

    @Override
    public final CachedIndexList<T> build() {

        return listAllocator.copyToImmutable(getList());
    }

    @Override
    public HeapIndexList<T> buildHeapAllocated() {

        return getList().makeFromElementsAndRecreate(this, (c, e, n, i) -> new HeapIndexList<>(AllocationType.HEAP, c, e, n));
    }

    @Override
    CachedIndexList<T> empty() {

        return CachedIndexList.empty();
    }
}
