package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

public final class HeapMutableIndexList<T>

        extends MutableObjectIndexList<T, IHeapIndexList<T>, IHeapIndexListBuilder<T>, IHeapIndexListAllocator<T>>
        implements IHeapMutableIndexList<T> {

    static <T> HeapMutableIndexList<T> create(IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);

        return new HeapMutableIndexList<>(AllocationType.HEAP, createElementsArray);
    }

    static <T> HeapMutableIndexList<T> create(IntFunction<T[]> createElementsArray, int initialCapacity) {

        Objects.requireNonNull(createElementsArray);
        Checks.isInitialCapacity(initialCapacity);

        return new HeapMutableIndexList<>(AllocationType.HEAP, createElementsArray, initialCapacity);
    }

    static <T> HeapMutableIndexList<T> create(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {

        AllocationType.checkIsHeap(allocationType);
        Objects.requireNonNull(createElementsArray);
        Checks.isInitialCapacity(initialCapacity);

        return new HeapMutableIndexList<>(allocationType, createElementsArray, initialCapacity);
    }

    static <T> HeapMutableIndexList<T> copyOf(IntFunction<T[]> createElementsArray, IBaseIndexList<T> toCopy) {

        Objects.requireNonNull(createElementsArray);
        Objects.requireNonNull(toCopy);

        return new HeapMutableIndexList<>(createElementsArray, toCopy, Function.identity());
    }

    static <T, U> HeapMutableIndexList<T> copyOf(IntFunction<T[]> createElementsArray, IBaseIndexList<U> toCopy, Function<U, T> mapper) {

        Objects.requireNonNull(createElementsArray);
        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(mapper);

        return new HeapMutableIndexList<>(createElementsArray, toCopy, mapper);
    }

    public HeapMutableIndexList(IntFunction<T[]> createElementsArray) {
        super(AllocationType.HEAP, createElementsArray);
    }

    public HeapMutableIndexList(IntFunction<T[]> createElementsArray, int initialCapacity) {
        this(AllocationType.HEAP, createElementsArray, initialCapacity);
    }

    HeapMutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    HeapMutableIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    @Override
    public IHeapIndexList<T> copyToImmutable(IHeapIndexListAllocator<T> immutableAllocator) {

        return new HeapIndexList<>(this);
    }

    private <U> HeapMutableIndexList(IntFunction<T[]> createElementsArray, IBaseIndexList<U> toCopy, Function<U, T> mapper) {
        super(AllocationType.HEAP, createElementsArray, toCopy, mapper);
    }

    private HeapMutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }
}
