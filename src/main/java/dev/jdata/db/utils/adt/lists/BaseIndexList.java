package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ICapacity;

abstract class BaseIndexList<T> extends BaseArrayList<T> implements ICapacity, IIndexListGetters<T> {

    BaseIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createArray, IIndexList<T> toCopy) {
        super(allocationType, createArray, toCopy);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createArray, int initialCapacity) {
        super(allocationType, createArray, initialCapacity);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createArray, T[] instances) {
        super(allocationType, createArray, instances);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createArray, T[] instances, int numElements) {
        super(allocationType, createArray, instances, numElements);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createArray) {
        super(allocationType, createArray);
    }

    BaseIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }
}
