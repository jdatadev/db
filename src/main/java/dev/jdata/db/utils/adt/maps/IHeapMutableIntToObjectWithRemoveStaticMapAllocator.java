package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

public interface IHeapMutableIntToObjectWithRemoveStaticMapAllocator<V>

        extends IMutableIntToObjectWithRemoveStaticMapAllocator<V, IHeapMutableIntToObjectWithRemoveStaticMap<V>> {

    static <V> IHeapMutableIntToObjectWithRemoveStaticMapAllocator<V> create(IntFunction<V[]> createValuesArray) {

        return HeapMutableIntToObjectWithRemoveNonBucketMapAllocator.create(createValuesArray);
    }
}
