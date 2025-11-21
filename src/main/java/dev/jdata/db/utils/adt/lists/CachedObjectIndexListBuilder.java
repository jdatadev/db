package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.ICacheableMarker;

final class CachedObjectIndexListBuilder<T>

        extends ObjectIndexListBuilder<T, ICachedIndexList<T>, IHeapIndexList<T>, MutableObjectIndexList<T>, ICachedIndexListBuilder<T>>
        implements ICacheableMarker, ICachedIndexListBuilder<T> {

    CachedObjectIndexListBuilder(AllocationType allocationType, MutableObjectIndexListAllocator<T, ?, ? extends MutableObjectIndexList<T>> mutableObjectIndexListAllocator) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY, mutableObjectIndexListAllocator);
    }

    CachedObjectIndexListBuilder(AllocationType allocationType, int initialCapacity,
            MutableObjectIndexListAllocator<T, ?, ? extends MutableObjectIndexList<T>> mutableObjectIndexListAllocator) {
        this(allocationType, initialCapacity, mutableObjectIndexListAllocator, (t, c, a) -> a.allocateMutableInstance(c));
    }

    private <P> CachedObjectIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MutableObjectIndexList<T>, P> allocator) {
        super(allocationType, minimumCapacity, parameter, allocator);
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
    protected ICachedIndexList<T> withArray(AllocationType allocationType, T[] elementsArray, int numElements) {

        checkWithArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return CachedObjectIndexList.withArray(allocationType, elementsArray, numElements);
    }

    @Override
    protected IHeapIndexList<T> withHeapArray(AllocationType allocationType, T[] elementsArray, int numElements) {

        checkWithHeapArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return HeapObjectIndexList.withArray(allocationType, elementsArray, numElements);
    }
}
