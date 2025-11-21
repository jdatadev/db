package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;

final class HeapMutableObjectIndexList<T> extends MutableObjectIndexList<T> implements IHeapMutableIndexList<T> {

    static <T> HeapMutableObjectIndexList<T> create(AllocationType allocationType, IntFunction<T[]> createElementsArray) {

        checkMutableCreateParameters(allocationType, AllocationMechanism.HEAP);

        return new HeapMutableObjectIndexList<>(allocationType, createElementsArray);
    }

    static <T> HeapMutableObjectIndexList<T> create(AllocationType allocationType, int initialCapacity, IntFunction<T[]> createElementsArray) {

        checkMutableCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);
        Objects.requireNonNull(createElementsArray);

        return new HeapMutableObjectIndexList<>(allocationType, initialCapacity, createElementsArray);
    }

    static <T> HeapMutableObjectIndexList<T> of(AllocationType allocationType, T[] instances) {

        checkOfInstancesParameters(allocationType, AllocationMechanism.HEAP, instances);

        return new HeapMutableObjectIndexList<>(AllocationType.HEAP, instances);
    }

    static <T> HeapMutableObjectIndexList<T> copyToMutable(AllocationType allocationType, IObjectIterableElementsView<T> mutableFrom, IntFunction<T[]> createElementsArray) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.HEAP, mutableFrom);
        Objects.requireNonNull(createElementsArray);

        return new HeapMutableObjectIndexList<>(allocationType, mutableFrom, createElementsArray);
    }

    static <T> HeapMutableObjectIndexList<T> copyOf(AllocationType allocationType, IBaseObjectIndexList<T> toCopy, IntFunction<T[]> createElementsArray) {

        checkCopyOfParameters(allocationType, AllocationMechanism.HEAP, toCopy);
        Objects.requireNonNull(toCopy);

        return new HeapMutableObjectIndexList<>(allocationType, createElementsArray, toCopy);
    }

    static <T, U> HeapMutableObjectIndexList<T> copyOf(AllocationType allocationType, IBaseObjectIndexList<U> toCopy, IntFunction<T[]> createElementsArray,
            Function<U, T> mapper) {

        checkCopyOfParameters(allocationType, AllocationMechanism.HEAP, toCopy);
        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(createElementsArray);
        Objects.requireNonNull(mapper);

        return new HeapMutableObjectIndexList<>(allocationType, toCopy, createElementsArray, mapper);
    }

    private HeapMutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    private HeapMutableObjectIndexList(AllocationType allocationType, int initialCapacity, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    private HeapMutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IBaseObjectIndexList<T> toCopy) {
        this(allocationType, toCopy, createElementsArray, Function.identity());
    }

    private HeapMutableObjectIndexList(AllocationType allocationType, IObjectIterableElementsView<T> mutableFrom, IntFunction<T[]> createElementsArray) {
        super(allocationType, mutableFrom, createElementsArray);
    }

    private <U> HeapMutableObjectIndexList(AllocationType allocationType, IBaseObjectIndexList<U> toCopy, IntFunction<T[]> createElementsArray, Function<U, T> mapper) {
        super(allocationType, createElementsArray, toCopy, mapper);
    }

    private HeapMutableObjectIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }
}
