package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

public interface ICachedMutableIntToObjectWithRemoveStaticMapAllocator<V>

        extends IMutableIntToObjectWithRemoveStaticMapAllocator<V, ICachedMutableIntToObjectWithRemoveStaticMap<V>> {

    public static <V> ICachedMutableIntToObjectWithRemoveStaticMapAllocator<V> create(IntFunction<V[]> createValuesArray) {

        Objects.requireNonNull(createValuesArray);

        return new CachedMutableIntToObjectWithRemoveNonBucketMapAllocator<>(createValuesArray);
    }
}
