package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

abstract class ObjectArray<T> extends BaseObjectArray<T> implements IObjectArray<T> {

    ObjectArray(AllocationType allocationType, T[] elements, IntFunction<T[]> createElementsArray) {
        super(allocationType, elements, elements.length, false, createElementsArray);
    }

    ObjectArray(AllocationType allocationType, BaseObjectArray<T> toCopy) {
        super(allocationType, toCopy);
    }

    @Override
    final T[] reallocate(T[] elementsArray, int newCapacity) {

        checkReallocateParameters(elementsArray, elementsArray.length, newCapacity);

        throw new UnsupportedOperationException();
    }

    @Override
    final void clearElementsArray(T[] elementsArray, int startIndex, int numElements) {

        checkClearElementsArrayParameters(elementsArray, elementsArray.length, startIndex, numElements);

        throw new UnsupportedOperationException();
    }
}
