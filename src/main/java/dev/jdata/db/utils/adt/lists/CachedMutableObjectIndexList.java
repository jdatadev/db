package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;

final class CachedMutableObjectIndexList<T> extends MutableObjectIndexList<T> implements ICachedMutableIndexList<T> {

    static <T> CachedMutableObjectIndexList<T> create(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {

        checkMutableCreateParameters(allocationType, AllocationMechanism.CACHE, initialCapacity);
        Objects.requireNonNull(createElementsArray);

        return new CachedMutableObjectIndexList<>(allocationType, createElementsArray, initialCapacity);
    }

    static <T> CachedMutableObjectIndexList<T> copyToMutable(AllocationType allocationType, IObjectIterableElementsView<T> mutableFrom, IntFunction<T[]> createElementsArray) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.CACHE, mutableFrom);
        Objects.requireNonNull(createElementsArray);

        return new CachedMutableObjectIndexList<>(allocationType, mutableFrom, createElementsArray);
    }

    private CachedMutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    private CachedMutableObjectIndexList(AllocationType allocationType, IObjectIterableElementsView<T> mutableFrom, IntFunction<T[]> createElementsArray) {
        super(allocationType, mutableFrom, createElementsArray);
    }
}
