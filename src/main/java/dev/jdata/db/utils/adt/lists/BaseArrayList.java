package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseArrayList<T> extends BaseADTList<T, T, T> {

    static int increaseCapacity(int capacity) {

        Checks.isIntCapacity(capacity);

        return capacity << 1;
    }

    abstract int getElementsCapacity();

    final IntFunction<T> createElementsArray;
    private T elementsArray;
    private int numElements;

    BaseArrayList(AllocationType allocationType) {
        super(allocationType);

        this.createElementsArray = null;
        this.elementsArray = null;
        this.numElements = 0;
    }

    BaseArrayList(AllocationType allocationType, IntFunction<T> createElementsArray) {
        super(allocationType);

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
        this.elementsArray = null;
        this.numElements = 0;
    }

    private BaseArrayList(AllocationType allocationType, IntFunction<T> createElementsArray, int numElements) {
        super(allocationType);

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
        this.elementsArray = null;
        this.numElements = Checks.isIntNumElements(numElements);
    }

    BaseArrayList(AllocationType allocationType, T array, int numElements) {
        super(allocationType);

        this.createElementsArray = null;
        this.elementsArray = Objects.requireNonNull(array);
        this.numElements = Checks.isIntNumElements(numElements);
    }

    BaseArrayList(AllocationType allocationType, IntFunction<T> createElementsArray, T array, int numElements) {
        super(allocationType);

        this.createElementsArray = Objects.requireNonNull(createElementsArray);
        this.elementsArray = Objects.requireNonNull(array);
        this.numElements = Checks.isIntNumElements(numElements);
    }

    @Override
    public final boolean isEmpty() {

        return numElements == 0;
    }

    @Override
    public final long getNumElements() {

        return numElements;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, T, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, createElementsArray, elementsArray, numElements, parameter);
    }

    @Override
    protected final void recreateElements() {

        recreateElements(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    protected final void resetToNull() {

        this.elementsArray = null;
        this.numElements = 0;
    }

    protected final void clearElements() {

        this.numElements = 0;
    }

    final void initializeArrayList(T values, int numElements) {

        Checks.isIntNumElements(numElements);

        checkIsAllocatedRenamed();

        this.elementsArray = values;
        this.numElements = numElements;
    }

    final T getElementsArray() {

        return elementsArray;
    }

    final int getIntNumElements() {

        return numElements;
    }

    final void setNumElements(int numElements) {

        this.numElements = Checks.isIntNumElements(numElements);
    }

    final void incrementNumElements() {

        ++ numElements;
    }

    final void decrementNumElements() {

        -- numElements;
    }

    final void increaseNumElements(int toAddNumElements) {

        Checks.isAboveZero(toAddNumElements);

        numElements += toAddNumElements;
    }

    final void decreaseNumElements(int toSubtractNumElements) {

        Checks.isAboveZero(toSubtractNumElements);
        Checks.isLessThanOrEqualTo(toSubtractNumElements, numElements);

        numElements -= toSubtractNumElements;
    }

    final T recreateArray(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        checkIsAllocatedRenamed();

        return this.elementsArray = createElementsArray.apply(initialCapacity);
    }

    final void setArray(T elementsArray) {

        Objects.requireNonNull(elementsArray);
        Checks.areNotSame(elementsArray, this.elementsArray);

        checkIsAllocatedRenamed();

        this.elementsArray = elementsArray;
    }

    private void recreateElements(int initialCapacity) {

        recreateArray(initialCapacity);

        this.numElements = 0;
    }
}
