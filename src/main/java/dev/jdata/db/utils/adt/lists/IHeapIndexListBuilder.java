package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapIndexListBuilder<T> extends IIndexListBuilder<T, IHeapIndexList<T>, IHeapIndexList<T>> {

    public static <T> IHeapIndexListBuilder<T> create(IntFunction<T[]> createElementsArray) {

        return new HeapObjectIndexListBuilder<>(AllocationType.HEAP, createElementsArray);
    }

    public static <T> IHeapIndexListBuilder<T> create(IntFunction<T[]> createElementsArray, int initialCapacity) {

        Objects.requireNonNull(createElementsArray);
        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapObjectIndexListBuilder<>(AllocationType.HEAP, createElementsArray, initialCapacity);
    }
}
