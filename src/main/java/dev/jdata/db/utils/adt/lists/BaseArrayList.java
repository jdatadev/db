package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseArrayList<T> extends BaseADTList {

    final IntFunction<T> createElementsArray;
    T elementsArray;
    int numElements;

    BaseArrayList(AllocationType allocationType) {
        super(allocationType);

        this.createElementsArray = null;
        this.elementsArray = null;
        this.numElements = 0;
    }

    BaseArrayList(AllocationType allocationType, IntFunction<T> createElementsArray) {
        super(allocationType);

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
    }

    BaseArrayList(AllocationType allocationType, T array, int numElements) {
        super(allocationType);

        this.createElementsArray = null;
        this.elementsArray = Objects.requireNonNull(array);
        this.numElements = Checks.isNumElements(numElements);
    }

    BaseArrayList(AllocationType allocationType, IntFunction<T> createArray, T array, int numElements) {
        super(allocationType);

        this.createElementsArray = Objects.requireNonNull(createArray);
        this.elementsArray = Objects.requireNonNull(array);
        this.numElements = Checks.isNumElements(numElements);
    }

    final void initialize(T values, int numElements, int valuesLength) {

        checkIsAllocated();

        if (values != null) {

            Checks.checkNumElements(numElements, valuesLength);
        }
        else {
            Checks.isNumElements(numElements);
        }

        this.elementsArray = values;
        this.numElements = numElements;
    }

    final void recreateArrays(int initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        checkIsAllocated();

        this.elementsArray = createElementsArray.apply(initialCapacity);
        this.numElements = 0;
    }

    final void resetToNullAndDispose() {

        setDisposed();

        this.elementsArray = null;
        this.numElements = 0;
    }

    @FunctionalInterface
    interface MakeFromElementsFunction<T, P, R> {

        R apply(T elements, int numElements, P parameter);
    }

    final <P, R> R makeFromElements(P parameter, MakeFromElementsFunction<T, P, R> makeFromElements) {

        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(elementsArray, numElements, parameter);
    }

    @Override
    public final boolean isEmpty() {

        return numElements == 0;
    }

    @Override
    public final long getNumElements() {

        return numElements;
    }

    final void clearElements() {

        this.numElements = 0;
    }
}
