package dev.jdata.db.utils.allocators;

import java.util.Objects;

import dev.jdata.db.engine.database.SQLExpressionEvaluatorParameter.ElementAllocator;
import dev.jdata.db.utils.checks.Checks;

public interface IArrayAllocator<T> {

    T[] allocateArray(int minimumCapacity);

    void freeArray(T[] array);

    default T[] allocateArray(int numElements, ElementAllocator<T> elementAllocator) {

        Checks.isLengthAboveZero(numElements);
        Objects.requireNonNull(elementAllocator);

        final T[] result = allocateArray(numElements);

        for (int i = 0; i < numElements; ++ i) {

            result[i] = elementAllocator.allocateElement();
        }

        return result;
    }

    default void freeArray(T[] array, int numElements, ElementAllocator<T> elementAllocator) {

        Objects.requireNonNull(array);
        Checks.checkNumElements(array, numElements);
        Objects.requireNonNull(elementAllocator);

        for (int i = 0; i < numElements; ++ i) {

            elementAllocator.freeElement(array[i]);

            array[i] = null;
        }
    }
}
