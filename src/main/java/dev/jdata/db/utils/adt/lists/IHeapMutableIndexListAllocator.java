package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

public interface IHeapMutableIndexListAllocator<T> extends IMutableIndexListAllocator<T, IHeapMutableIndexList<T>> {

    public static <T> IHeapMutableIndexListAllocator<T> create(IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);

        return new HeapMutableObjectIndexListAllocator<>(createElementsArray);
    }
}
