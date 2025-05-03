package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ICapacity;

abstract class BaseIndexList<T> extends BaseArrayList<T> implements ICapacity, IIndexListGetters<T> {

    BaseIndexList() {

    }

    BaseIndexList(IntFunction<T[]> createArray, IIndexList<T> toCopy) {
        super(createArray, toCopy);
    }

    BaseIndexList(IntFunction<T[]> createArray, int initialCapacity) {
        super(createArray, initialCapacity);
    }

    BaseIndexList(IntFunction<T[]> createArray, T[] instances) {
        super(createArray, instances);
    }

    BaseIndexList(IntFunction<T[]> createArray, T[] instances, int numElements) {
        super(createArray, instances, numElements);
    }

    BaseIndexList(IntFunction<T[]> createArray) {
        super(createArray);
    }

    BaseIndexList(T[] instances) {
        super(instances);
    }
}
