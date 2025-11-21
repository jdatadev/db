package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

public interface IHeapMutableLongToObjectDynamicMapAllocator<V> extends IMutableLongToObjectDynamicMapAllocator<V, IHeapMutableLongToObjectDynamicMap<V>> {

    public static <V> IHeapMutableLongToObjectDynamicMapAllocator<V> create(IntFunction<V[]> createValuesArray) {

        Objects.requireNonNull(createValuesArray);

        return new HeapMutableLongToObjectMaxDistanceNonBucketMapAllocator<>(createValuesArray);
    }
}
