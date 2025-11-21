package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapIndexListBuilder<T> extends IIndexListBuilder<T, IHeapIndexList<T>, IHeapIndexList<T>> {

    public static <T> IHeapIndexListBuilder<T> create(IntFunction<T[]> createElementsArray) {

        return HeapObjectIndexListBuilder.create(AllocationType.HEAP, createElementsArray);
    }

    public static <T> IHeapIndexListBuilder<T> create(int initialCapacity, IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);
        Checks.isIntInitialCapacityAtOrAboveZero(initialCapacity);

        return HeapObjectIndexListBuilder.create(AllocationType.HEAP, initialCapacity, createElementsArray);
    }
}
