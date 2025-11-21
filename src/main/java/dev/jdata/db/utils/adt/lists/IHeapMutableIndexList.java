package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableIndexList<T> extends IMutableIndexList<T> {

    public static <T> IHeapMutableIndexList<T> create(IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);

        return new HeapMutableObjectIndexList<>(AllocationType.HEAP, createElementsArray);
    }

    public static <T> IHeapMutableIndexList<T> create(IntFunction<T[]> createElementsArray, int initialCapacity) {

        Objects.requireNonNull(createElementsArray);
        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableObjectIndexList<>(AllocationType.HEAP, createElementsArray, initialCapacity);
    }

    @SafeVarargs
    public static <T> IHeapMutableIndexList<T> of(T ... instances) {

        Objects.requireNonNull(instances);

        return HeapMutableObjectIndexList.of(AllocationType.HEAP, instances);
    }

    public static <T> IHeapMutableIndexList<T> copyOf(IntFunction<T[]> createElementsArray, IBaseObjectIndexList<T> toCopy) {

        Objects.requireNonNull(createElementsArray);
        Objects.requireNonNull(toCopy);

        return new HeapMutableObjectIndexList<>(AllocationType.HEAP, createElementsArray, toCopy, Function.identity());
    }

    public static <T, U> IHeapMutableIndexList<T> copyOf(IntFunction<T[]> createElementsArray, IBaseObjectIndexList<U> toCopy, Function<U, T> mapper) {

        Objects.requireNonNull(createElementsArray);
        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(mapper);

        return new HeapMutableObjectIndexList<>(AllocationType.HEAP, createElementsArray, toCopy, mapper);
    }
}
