package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.adt.capacity.Capacity;
import dev.jdata.db.utils.adt.elements.IByIndexOrderedOnlyElementsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.ObjIntFunction;

abstract class BaseArrayList<T> extends BaseADTList<T, T, T> implements IByIndexOrderedOnlyElementsView {

    final <P> T checkArrayCapacity(int numElementsToAdd, P parameter, ToIntFunction<T> arrayLengthGetter, ObjIntFunction<P, T> createArray) {

        Checks.isIntNumElements(numElementsToAdd);
        Objects.requireNonNull(arrayLengthGetter);
        Objects.requireNonNull(createArray);

        final T thisArray = elementsArray;
        final int arrayLength = arrayLengthGetter.applyAsInt(thisArray);

        final int numElementsBeforeAdd = numElements;

        final int requiredArrayLength = numElementsBeforeAdd + numElementsToAdd;

        final T dstArray;

        if (requiredArrayLength > arrayLength) {

            final int newCapacity = Capacity.computeIncreasedIntCapacity(arrayLength, requiredArrayLength);

            dstArray = createArray.apply(parameter, newCapacity);

            System.arraycopy(thisArray, 0, dstArray, 0, numElementsBeforeAdd);
        }
        else {
            dstArray = thisArray;
        }

        return dstArray;
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

        checkMakeFromElementsParameters(allocationType, parameter, makeFromElements);

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

    final int getAndIncrementNumElements() {

        return numElements ++;
    }

    final void incrementNumElements() {

        ++ numElements;
    }

    final void decrementNumElements() {

        -- numElements;
    }

    final int getAndIncreaseNumElements(int toAddNumElements) {

        Checks.isAboveZero(toAddNumElements);

        final int result = numElements;

        numElements += toAddNumElements;

        return result;
    }

    final void decreaseNumElements(int toSubtractNumElements) {

        Checks.isAboveZero(toSubtractNumElements);
        Checks.isLessThanOrEqualTo(toSubtractNumElements, numElements);

        numElements -= toSubtractNumElements;
    }

    final T recreateArray(int initialCapacity) {

        Checks.isIntInitialCapacityAboveZero(initialCapacity);

        checkIsAllocatedRenamed();

        return this.elementsArray = createElementsArray.apply(initialCapacity);
    }

    private void recreateElements(int initialCapacity) {

        recreateArray(initialCapacity);

        this.numElements = 0;
    }
}
