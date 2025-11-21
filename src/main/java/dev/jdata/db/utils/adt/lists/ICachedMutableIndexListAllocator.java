package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

public interface ICachedMutableIndexListAllocator<T> extends IMutableIndexListAllocator<T, ICachedMutableIndexList<T>> {

    public static <T> ICachedMutableIndexListAllocator<T> create(IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);

        return new CachedMutableObjectIndexListAllocator<>(createElementsArray);
    }
}
