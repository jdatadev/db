package dev.jdata.db.utils.adt.lists;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIndexList<T> extends IMutableIndexList<T> {

    public static <T> IHeapMutableIndexList<T> create(IntFunction<T[]> createElementsArray) {

        return HeapMutableObjectIndexList.create(AllocationType.HEAP, createElementsArray);
    }

    public static <T> IHeapMutableIndexList<T> create(int initialCapacity, IntFunction<T[]> createElementsArray) {

        return HeapMutableObjectIndexList.create(AllocationType.HEAP, initialCapacity, createElementsArray);
    }

    @SafeVarargs
    public static <T> IHeapMutableIndexList<T> of(T ... instances) {

        return HeapMutableObjectIndexList.of(AllocationType.HEAP, instances);
    }

    public static <T> IHeapMutableIndexList<T> copyOf(IntFunction<T[]> createElementsArray, IBaseObjectIndexList<T> toCopy) {

        return HeapMutableObjectIndexList.copyOf(AllocationType.HEAP, toCopy, createElementsArray);
    }

    public static <T, U> IHeapMutableIndexList<T> copyOf(IntFunction<T[]> createElementsArray, IBaseObjectIndexList<U> toCopy, Function<U, T> mapper) {

        return HeapMutableObjectIndexList.copyOf(AllocationType.HEAP, toCopy, createElementsArray, mapper);
    }
}
