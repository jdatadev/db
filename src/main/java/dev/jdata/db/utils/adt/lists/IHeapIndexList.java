package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapIndexList<T> extends IIndexList<T>, IHeapContainsMarker {

    public static <T> IHeapIndexList<T> empty() {

        return HeapObjectIndexList.empty();
    }

    public static <T> IHeapIndexList<T> of(T instance) {

        Objects.requireNonNull(instance);

        return HeapObjectIndexList.of(AllocationType.HEAP, instance);
    }

    @SafeVarargs
    public static <T> IHeapIndexList<T> of(T ... instances) {

        Objects.requireNonNull(instances);

        return HeapObjectIndexList.of(AllocationType.HEAP, instances);
    }

    public static <T> IHeapIndexList<T> copyOf(IntFunction<T[]> createElementsArray, IIndexListView<T> elements) {

        Objects.requireNonNull(createElementsArray);
        Objects.requireNonNull(elements);

        return HeapObjectIndexList.copyIndexListView(AllocationType.HEAP, createElementsArray, elements);
    }
}
