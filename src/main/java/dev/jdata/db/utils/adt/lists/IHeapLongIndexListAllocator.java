package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

public interface IHeapLongIndexListAllocator extends ILongIndexListAllocator<IHeapLongIndexList, IHeapMutableLongIndexList, IHeapLongIndexListBuilder> {

    public static <T> IHeapIndexListAllocator<T> create(IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);

        return new HeapObjectIndexListAllocator<>(createElementsArray);
    }
}
