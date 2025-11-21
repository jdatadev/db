package dev.jdata.db.utils.adt.lists;

import java.util.function.Function;
import java.util.function.IntFunction;

abstract class BaseIndexList<T> extends BaseObjectArrayList<T> implements IObjectIndexListView<T> {

    BaseIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseIndexList(IntFunction<T[]> createElementsArray, IBaseIndexList<T> toCopy) {
        super(createElementsArray, toCopy);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IBaseIndexList<T> toCopy) {
        super(allocationType, createElementsArray, toCopy);
    }

    <U> BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IBaseIndexList<U> toCopy, Function<U, T> mapper) {
        super(allocationType, createElementsArray, toCopy, mapper);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, instance);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances) {
        super(allocationType, createElementsArray, instances);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    BaseIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    BaseIndexList(BaseIndexList<T> toCopy) {
        super(toCopy);
    }
}
