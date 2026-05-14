package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

public interface IHeapLongToObjectDynamicMapAllocator<V>

        extends ILongToObjectDynamicMapAllocator<V, IHeapLongToObjectDynamicMap<V>, IHeapMutableLongToObjectDynamicMap<V>, IHeapLongToObjectDynamicMapBuilder<V>> {

    static <V> IHeapLongToObjectDynamicMapAllocator<V> create(IntFunction<V[]> createValuesArray) {

        Objects.requireNonNull(createValuesArray);

        return new HeapLongToObjectDynamicMapAllocator<>(createValuesArray);
    }
}
