package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

public interface ICachedMutableLongToObjectDynamicMapAllocator<V> extends IMutableLongToObjectDynamicMapAllocator<V, ICachedMutableLongToObjectDynamicMap<V>> {

    public static <V> ICachedMutableLongToObjectDynamicMapAllocator<V> create(IntFunction<V[]> createValuesArray) {

        Objects.requireNonNull(createValuesArray);

        return new CachedMutableLongToObjectDynamicMapAllocator<>(createValuesArray);
    }
}
