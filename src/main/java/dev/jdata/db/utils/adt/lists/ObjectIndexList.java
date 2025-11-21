package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

abstract class ObjectIndexList<T> extends BaseObjectIndexList<T> implements IBaseObjectIndexList<T> {

    ObjectIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    ObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    ObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, instance);
    }

    ObjectIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    ObjectIndexList(AllocationType allocationType, T[] instances, int numElements) {
        super(allocationType, instances, numElements);
    }

    ObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    ObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IIndexListView<T> toCopy, int numElements) {
        super(allocationType, createElementsArray, toCopy, numElements);
    }

    ObjectIndexList(AllocationType allocationType, BaseObjectIndexList<T> toCopy) {
        super(allocationType, toCopy);
    }
}
