package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapMutableObjectIndexList<T> extends MutableObjectIndexList<T> implements IHeapMutableIndexList<T> {

    @SafeVarargs
    public static <T> HeapMutableObjectIndexList<T> of(AllocationType allocationType, T ... instances) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(instances);

        return new HeapMutableObjectIndexList<>(allocationType, instances);
    }

    AllocationType.checkIsHeap(allocationType);

    HeapMutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    HeapMutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    <U> HeapMutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IBaseObjectIndexList<U> toCopy, Function<U, T> mapper) {
        super(allocationType, createElementsArray, toCopy, mapper);
    }

    private HeapMutableObjectIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }
}
