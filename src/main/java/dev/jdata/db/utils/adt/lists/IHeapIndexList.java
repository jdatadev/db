package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapIndexList<T> extends IIndexList<T>, IHeapContainsMarker {

    public static <T> IHeapIndexList<T> empty() {

        return HeapObjectIndexList.empty();
    }

    public static <T> IHeapIndexList<T> of(T instance) {

        return HeapObjectIndexList.of(AllocationType.HEAP, instance);
    }

    @SafeVarargs
    public static <T> IHeapIndexList<T> of(T ... instances) {

        return instances.length != 0 ? HeapObjectIndexList.of(AllocationType.HEAP, instances) : HeapObjectIndexList.empty();
    }

    public static <T> IHeapIndexList<T> copyOf(IIndexListView<T> elements, IntFunction<T[]> createElementsArray) {

        return HeapObjectIndexList.copyOf(AllocationType.HEAP, elements, createElementsArray);
    }
}
