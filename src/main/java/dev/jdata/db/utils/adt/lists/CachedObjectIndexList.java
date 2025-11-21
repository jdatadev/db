package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

final class CachedObjectIndexList<T> extends ObjectIndexList<T> implements ICachedIndexList<T> {

    private static final CachedObjectIndexList<?> emptyList = new CachedObjectIndexList<>(AllocationType.HEAP_CONSTANT);

    @SuppressWarnings("unchecked")
    static <T> CachedObjectIndexList<T> empty() {

        return (CachedObjectIndexList<T>)emptyList;
    }

    static <T> CachedObjectIndexList<T> withArray(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] values, int numElements) {

        checkCachedWithArrayParameters(allocationType, values, numElements);

        return new CachedObjectIndexList<>(allocationType, createElementsArray, values, numElements);
    }

    CachedObjectIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private CachedObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    private CachedObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    @Override
    public IHeapIndexList<T> toHeapAllocated() {

        return HeapObjectIndexList.copyImmutableIndexList(AllocationType.HEAP, createElementsArray, this);
    }
}
