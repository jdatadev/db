package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public final class HeapIndexListAllocator<T>

        extends IndexListAllocator<T, IHeapIndexList<T>, HeapMutableIndexList<T>, IHeapIndexList<T>, IHeapIndexListBuilder<T>, HeapIndexListAllocator<T>>
        implements IHeapIndexListAllocator<T> {

    private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

    private final IntFunction<T[]> createElementsArray;
    private final HeapMutableIndexListAllocator<T> mutableIndexListAllocator;

    public HeapIndexListAllocator(IntFunction<T[]> createElementsArray) {
        this(createElementsArray, new HeapMutableIndexListAllocator<T>(createElementsArray));
    }

    private HeapIndexListAllocator(IntFunction<T[]> createElementsArray, HeapMutableIndexListAllocator<T> mutableIndexListAllocator) {

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
        this.mutableIndexListAllocator = Objects.requireNonNull(mutableIndexListAllocator);
    }

    @Override
    public IHeapIndexListBuilder<T> createBuilder(long minimumCapacity) {

        Checks.isCapacity(minimumCapacity);

        return new HeapIndexListBuilder<>(ALLOCATION_TYPE, Capacity.intCapacity(minimumCapacity), this);
    }

    @Override
    public void freeBuilder(HeapIndexListBuilder<T> builder) {

        Objects.requireNonNull(builder);
    }

    @Override
    protected HeapIndexList<T> allocateImmutableFrom(T[] values, int numElements) {

        return new HeapIndexList<>(ALLOCATION_TYPE, createElementsArray, values, numElements);
    }

    @Override
    public void freeImmutable(HeapIndexList<T> list) {

        Objects.requireNonNull(list);
    }

    @Override
    protected HeapMutableIndexList<T> allocateMutable(int minimumCapacity) {

        return mutableIndexListAllocator.allocateMutableIndexList(minimumCapacity);
    }

    @Override
    protected void freeMutable(HeapMutableIndexList<T> list) {

        Objects.requireNonNull(list);
    }

    @Override
    protected HeapIndexList<T> copyToImmutable(HeapMutableIndexList<T> mutableIndexList) {

        Objects.requireNonNull(mutableIndexList);

        return HeapIndexList.fromMutableIndexList(ALLOCATION_TYPE, mutableIndexList);
    }

    @Override
    protected HeapIndexList<T> emptyImmutable() {

        return HeapIndexList.empty();
    }
}
