package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

final class CachedMutableObjectIndexList<T> extends MutableObjectIndexList<T> implements ICachedMutableIndexList<T> {

    CachedMutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray, DEFAULT_INITIAL_CAPACITY);
    }

    CachedMutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }
}
