package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

public interface IHeapIndexListAllocator<T> extends IIndexListAllocator<T, IHeapIndexList<T>, IHeapMutableIndexList<T>, IHeapIndexListBuilder<T>> {

    public static <T> IHeapIndexListAllocator<T> create(IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);

        return new HeapObjectIndexListAllocator<>(createElementsArray);
    }
}
