package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.ICacheableMarker;

final class CachedObjectIndexListBuilder<T>

        extends ObjectIndexListBuilder<T, ICachedIndexList<T>, IHeapIndexList<T>, MutableObjectIndexList<T>, ICachedIndexListBuilder<T>>
        implements ICacheableMarker, ICachedIndexListBuilder<T> {

    private final IntFunction<T[]> createElementsArray;

    CachedObjectIndexListBuilder(AllocationType allocationType, MutableObjectIndexListAllocator<T, ?, ? extends MutableObjectIndexList<T>> mutableObjectIndexListAllocator,
            IntFunction<T[]> createElementsArray) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY, mutableObjectIndexListAllocator, createElementsArray);
    }

    CachedObjectIndexListBuilder(AllocationType allocationType, int initialCapacity,
            MutableObjectIndexListAllocator<T, ?, ? extends MutableObjectIndexList<T>> mutableObjectIndexListAllocator, IntFunction<T[]> createElementsArray) {
        this(allocationType, initialCapacity, mutableObjectIndexListAllocator, (t, c, a) -> a.allocateMutableInstance(c), createElementsArray);
    }

    private <P> CachedObjectIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MutableObjectIndexList<T>, P> allocator, IntFunction<T[]> createElementsArray) {
        super(allocationType, minimumCapacity, parameter, allocator);

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
    }

    @Override
    protected ICachedIndexList<T> empty() {

        return CachedObjectIndexList.empty();
    }

    @Override
    protected IHeapIndexList<T> heapEmpty() {

        return HeapObjectIndexList.empty();
    }

    @Override
    protected ICachedIndexList<T> withMakeElementsFrom(AllocationType allocationType, T[] makeElementsFrom, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return CachedObjectIndexList.withArray(allocationType, createElementsArray, makeElementsFrom, numElements);
    }

    @Override
    protected IHeapIndexList<T> withHeapMakeElementsFrom(AllocationType allocationType, T[] makeElementsFrom, int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return HeapObjectIndexList.withArray(allocationType, makeElementsFrom, numElements);
    }
}
