package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

public interface ICachedIndexListAllocator<T> extends IIndexListAllocator<T, ICachedIndexList<T>, ICachedMutableIndexList<T>, ICachedIndexListBuilder<T>> {

    public static <T> ICachedIndexListAllocator<T> create(IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);

        return new CachedObjectIndexListAllocator<>(createElementsArray, new CachedMutableObjectIndexListAllocator<T>(createElementsArray));
    }
}
