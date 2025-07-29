package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.BiFunction;

import dev.jdata.db.engine.database.SQLExpressionEvaluatorParameter.ElementAllocator;
import dev.jdata.db.utils.checks.Checks;

public interface IArrayAllocator<T> {

    T[] allocateArray(int minimumCapacity);

    default T[] allocateArrayCopy(T[] toCopy) {

        Objects.requireNonNull(toCopy);

        final int toCopyLength = toCopy.length;

        final T[] result = allocateArray(toCopyLength);

        System.arraycopy(toCopy, 0, result, 0, toCopyLength);

        return result;
    }

    default T[] allocateArrayCopy(T[] toCopy, int startIndex, int numElements) {

        Objects.requireNonNull(toCopy);
        Checks.checkFromIndexSize(startIndex, numElements, toCopy.length);

        final T[] result = allocateArray(numElements);

        System.arraycopy(toCopy, startIndex, result, 0, numElements);

        return result;
    }

    default <U, P> T[] allocateArrayCopy(U[] toCopy, P parameter, BiFunction<U, P, T> mapper) {

        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(mapper);

        final int toCopyLength = toCopy.length;

        final T[] result = allocateArray(toCopyLength);

        for (int i = 0; i < toCopyLength; ++ i) {

            result[i] = mapper.apply(toCopy[i], parameter);
        }

        return result;
    }

    default T[] reallocate(T[] src, int numElements) {

        if (numElements < 1) {

            throw new IllegalArgumentException();
        }

        final int srcLength = src.length;

        if (numElements <= srcLength) {

            throw new IllegalArgumentException();
        }

        final T[] result = allocateArray(numElements);

        System.arraycopy(src, 0, result, 0, srcLength);

        return result;
    }

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
