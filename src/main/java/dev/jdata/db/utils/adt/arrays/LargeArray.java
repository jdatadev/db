package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class LargeArray<O, I> extends LargeExponentArray {

    private final IntFunction<O> createOuterArray;

    private O array;

    abstract O copyOuterArray(O outerArray, int capacity);
    abstract int getOuterArrayLength(O outerArray);
    abstract I getInnerArray(O outerArray, int index);
    abstract int getInnerArrayLength(I innerArray);
    abstract void setInnerArrayLength(I innerArray, int length);
    abstract int getNumInnerElements(I innerArray);
    abstract I setInnerArray(O outerArray, int outerIndex, int innerArrayLength);

    protected LargeArray(int initialOuterCapacity, int innerCapacityExponent, int innerArrayLengthNumElements, IntFunction<O> createOuterArray) {
        super(innerCapacityExponent, 0, innerArrayLengthNumElements);

        Checks.isCapacity(initialOuterCapacity);
        Objects.requireNonNull(createOuterArray);

        this.createOuterArray = createOuterArray;

        this.array = initialOuterCapacity != 0 ? createOuterArray.apply(initialOuterCapacity) : null;
    }

    @Override
    public void reset() {

        final int numOuter = getNumOuterUtilizedEntries();

        for (int i = 0; i < numOuter; ++ i) {

            setInnerArrayLength(getInnerArray(array, i), 0);
        }

        super.reset();
    }

    final O getArray() {
        return array;
    }

    final void setArray(O array) {

        this.array = Objects.requireNonNull(array);
    }

    final <P> I checkCapacity(P parameter, ArrayClearer<I, P> arrayClearer) {

         return checkCapacity(1, parameter, arrayClearer);
    }

    @FunctionalInterface
    interface ArrayClearer<T, P> {

        void clear(T array, int startIndex, int numElements, P parameter);
    }

    final <P> void clearArrays(P parameter, ArrayClearer<I, P> arrayClearer) {

        Objects.requireNonNull(arrayClearer);

        final int numOuter = getNumOuterUtilizedEntries();

        final O outerArray = array;

        final int innerCapacity = getInnerCapacity();

        final int innerArrayLengthNumElements = getInnerArrayLengthNumElements();

        for (int i = 0; i < numOuter; ++ i) {

            final I a = getInnerArray(outerArray, i);

            if (arrayClearer != null) {

                arrayClearer.clear(a, innerArrayLengthNumElements, innerCapacity, parameter);
            }
        }
    }

    final <P> I checkCapacity(long numAdditional, P parameter, ArrayClearer<I, P> arrayClearer) {

        Checks.isLengthAboveZero(numAdditional);
        Objects.requireNonNull(arrayClearer);

        int numOuterAllocatedEntries = getNumOuterAllocatedEntries();
        int numOuterUtilizedEntries = getNumOuterUtilizedEntries();

        final int innerCapacity = getInnerCapacity();

        final int resultIndex;

        final int innerArrayLengthNumElements = getInnerArrayLengthNumElements();
        final boolean setInnerArrayLength = innerArrayLengthNumElements != 0;

        if (numOuterAllocatedEntries == 0) {

            final int numOuter = Integers.checkUnsignedLongToUnsignedInt(((numAdditional - 1) / innerCapacity) + 1);

            incrementNumOuterAllocatedEntries(numOuter);
            incrementNumOuterUtilizedEntries(numOuter);

            this.array = createOuterArray.apply(numOuter);

            final int innerNumAllocateElements = getInnerNumAllocateElements();

            for (int i = 0; i < numOuter; ++ i) {

                final I a = setInnerArray(array, i, innerNumAllocateElements);

                if (arrayClearer != null) {

                    arrayClearer.clear(a, innerArrayLengthNumElements, innerCapacity, parameter);
                }

                if (setInnerArrayLength) {

                    setInnerArrayLength(a, 0);
                }
            }

            resultIndex = 0;
        }
        else {
            long remainingAdditional = numAdditional;

            if (numOuterUtilizedEntries == 0) {

                incrementNumOuterUtilizedEntries();

                ++ numOuterUtilizedEntries;

                final I a = getInnerArray(array, 0);

                if (setInnerArrayLength) {

                    setInnerArrayLength(a, 0);
                }

                remainingAdditional -= Math.min(innerCapacity, numAdditional);

                resultIndex = 0;
            }
            else {
                if (setInnerArrayLength) {

                    final I innerArray = getInnerArray(array, numOuterUtilizedEntries - 1);

                    final int numInnerElements = getNumInnerElements(innerArray);

                    remainingAdditional -= Math.min(numInnerElements, numAdditional);

                    resultIndex = numInnerElements < innerCapacity
                            ? numOuterUtilizedEntries - 1
                            : numOuterUtilizedEntries;
                }
                else {
                    remainingAdditional = numAdditional;

                    resultIndex = numOuterUtilizedEntries;
                }
            }

            final int innerNumAllocateElements = getInnerNumAllocateElements();

            while (remainingAdditional != 0L) {

                if (numOuterUtilizedEntries == numOuterAllocatedEntries) {

                    final int outerArrayLength = getOuterArrayLength(array);

                    if (numOuterUtilizedEntries == outerArrayLength) {

                        this.array = copyOuterArray(array, outerArrayLength << 2);

                        setArray(array);
                    }

                    incrementNumOuterAllocatedEntries();

                    ++ numOuterAllocatedEntries;

                    final I a = setInnerArray(array, numOuterUtilizedEntries, innerNumAllocateElements);

                    if (arrayClearer != null) {

                        arrayClearer.clear(a, innerArrayLengthNumElements, innerCapacity, parameter);
                    }

                    if (setInnerArrayLength) {

                        setInnerArrayLength(a, 0);
                    }
                }

                incrementNumOuterUtilizedEntries();

                ++ numOuterUtilizedEntries;

                remainingAdditional -= Math.min(innerCapacity, remainingAdditional);
            }
        }

        return getInnerArray(array, resultIndex);
    }
}
