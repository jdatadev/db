package dev.jdata.db.utils.adt.lists;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.byindex.IObjectByIndexView;

abstract class BaseObjectIndexList<T> extends BaseObjectArrayList<T> implements IIndexListView<T> {

    BaseObjectIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    BaseObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    BaseObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, instance);
    }

    BaseObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances) {
        super(allocationType, createElementsArray, instances);
    }

    BaseObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    BaseObjectIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    BaseObjectIndexList(AllocationType allocationType, T[] instances, int numElements) {
        super(allocationType, instances, numElements);
    }

    BaseObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IObjectByIndexView<T> toCopy, int numElements) {
        super(allocationType, createElementsArray, toCopy, numElements);
    }

    <U> BaseObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IObjectByIndexView<U> toCopy, int numElements, Function<U, T> mapper) {
        super(allocationType, createElementsArray, toCopy, numElements, mapper);
    }

    BaseObjectIndexList(AllocationType allocationType, BaseObjectIndexList<T> toCopy) {
        super(allocationType, toCopy);
    }
}
