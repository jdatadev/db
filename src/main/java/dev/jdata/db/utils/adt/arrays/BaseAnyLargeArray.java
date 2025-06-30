package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseAnyLargeArray<O, I> implements IContains, IClearable {

    protected abstract long getInnerElementCapacity();

    protected abstract I getInnerArray(O outerArray, int index);
    protected abstract O copyOuterArray(O outerArray, int newCapacity);

    private final IntFunction<O> createOuterArray;
    private final boolean isNumInnerElementsRequired;

    private O outerArray;
    private int[] innerArrayNumElements;

    private int numOuterUtilizedEntries;

    BaseAnyLargeArray(int initialOuterCapacity, IntFunction<O> createOuterArray, boolean requiresInnerArrayNumElements) {

        Checks.isLengthAboveOrAtZero(initialOuterCapacity);

        this.createOuterArray = Objects.requireNonNull(createOuterArray);
        this.isNumInnerElementsRequired = requiresInnerArrayNumElements;

        if (initialOuterCapacity != 0) {

            this.outerArray = createOuterArray.apply(initialOuterCapacity);
            this.innerArrayNumElements = requiresInnerArrayNumElements ? new int[initialOuterCapacity] : null;
        }
        else {
            this.outerArray = null;
            this.innerArrayNumElements = null;
        }

        this.numOuterUtilizedEntries = 0;
    }

    @Override
    public final boolean isEmpty() {

        return numOuterUtilizedEntries == 0;
    }

    @Override
    public void clear() {

        if (innerArrayNumElements != null) {

            Arrays.fill(innerArrayNumElements, 0, getNumOuterUtilizedEntries(), 0);
        }

        this.numOuterUtilizedEntries = 0;
    }

    protected final O getOuterArray() {
        return outerArray;
    }

    protected final int getNumInnerElements(int outerIndex) {

        checkOuterIndex(outerIndex);

        return innerArrayNumElements[outerIndex];
    }

    final O reallocateOuterArrayAndInnerArrayNumElementsWithExistingLength(int outerArrayLength) {

        Checks.isLengthAboveZero(outerArrayLength);

        final int newCapacity = outerArrayLength << 2;

        return reallocateOuterArrayAndInnerArrayNumElements(newCapacity);
    }

    final O reallocateOuterArrayAndInnerArrayNumElements(int newCapacity) {

        Checks.isCapacity(newCapacity);

        final O result = copyOuterArray(outerArray, newCapacity);

        setOuterArray(result);

        if (isNumInnerElementsRequired()) {

            this.innerArrayNumElements = innerArrayNumElements != null
                    ? Arrays.copyOf(innerArrayNumElements, newCapacity)
                    : new int[newCapacity];
        }

        return result;
    }

    final void setOuterArray(O outerArray) {

        Checks.areNotSame(this.outerArray, outerArray);

        this.outerArray = Objects.requireNonNull(outerArray);
    }

    long getRemainderOfLastInnerArray(int outerIndex) {

        Checks.areEqual(outerIndex, numOuterUtilizedEntries - 1);

        if (!isNumInnerElementsRequired()) {

            throw new UnsupportedOperationException();
        }

        return Capacity.getRemainderOfLastInnerArray(outerIndex, numOuterUtilizedEntries, getInnerElementCapacity(), getNumInnerElements(outerIndex));
    }

    final <P> O allocateInitialOuterArrayAndInnerArrayElements(int numOuter) {

        Checks.isLengthAboveZero(numOuter);

        incrementNumOuterUtilizedEntries(numOuter, numOuter);

        final O outerArray = createOuterArray.apply(numOuter);

        allocateInitialNumInnerElementsIfRequired(numOuter);

        setOuterArray(outerArray);

        return outerArray;
    }

    final void allocateInitialNumInnerElementsIfRequired(int numToAllocate) {

        Checks.isLengthAboveZero(numToAllocate);

        if (isNumInnerElementsRequired()) {

            if (innerArrayNumElements != null) {

                throw new IllegalStateException();
            }

            this.innerArrayNumElements = new int[numToAllocate];
        }
    }

    final void clearNumInnerElementsIfRequired(int outerIndex) {

        setNumInnerElementsIfRequired(outerIndex, 0);
    }

    final void setNumInnerElementsIfRequired(int outerIndex, int numElements) {

        if (isNumInnerElementsRequired()) {

            setNumInnerElements(outerIndex, numElements);
        }
    }

    protected final void setNumInnerElements(int outerIndex, int numElements) {

        checkOuterIndex(outerIndex);
        Checks.isNumElements(numElements);

        innerArrayNumElements[outerIndex] = numElements;
    }

    final void incrementNumInnerElements(int outerIndex) {

        Checks.isIndex(outerIndex);

        ++ innerArrayNumElements[outerIndex];
    }

    @FunctionalInterface
    interface ArrayClearer<T, P> {

        void clear(T array, int startIndex, int numElements, P parameter);
    }

    final <P> void clearArrays(P parameter, int numOuter, int startIndex, int innerCapacity, ArrayClearer<I, P> arrayClearer) {

        Objects.requireNonNull(arrayClearer);

        final O outer = outerArray;

        for (int i = 0; i < numOuter; ++ i) {

            final I a = getInnerArray(outer, i);

            if (arrayClearer != null) {

                arrayClearer.clear(a, startIndex, innerCapacity, parameter);
            }
        }
    }

    protected final int getNumOuterUtilizedEntries() {

        return numOuterUtilizedEntries;
    }

    final void setNumOuterUtilizedEntries(int numOuterUtilizedEntries, int maxValue) {

        Checks.isGreaterThan(numOuterUtilizedEntries, this.numOuterUtilizedEntries);
        Checks.isLessThanOrEqualTo(numOuterUtilizedEntries, maxValue);

        this.numOuterUtilizedEntries = numOuterUtilizedEntries;
    }

    final void incrementNumOuterUtilizedEntries() {

        ++ numOuterUtilizedEntries;
    }

    final void incrementNumOuterUtilizedEntries(int numAdditional, int maxValue) {

        Checks.isLessThanOrEqualTo(numOuterUtilizedEntries + numAdditional, maxValue);

        numOuterUtilizedEntries += numAdditional;
    }

    private boolean isNumInnerElementsRequired() {

        return isNumInnerElementsRequired;
    }

    private int checkOuterIndex(int outerIndex) {

        return Checks.isIndex(outerIndex);
    }
}
