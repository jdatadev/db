package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.checks.Checks;

public abstract class LargeArray<O, I> extends LargeExponentArray implements IMutableElements {

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
    public final void clear() {

        final int numOuter = getNumOuterEntries();

        for (int i = 0; i < numOuter; ++ i) {

            setInnerArrayLength(getInnerArray(array, i), 0);
        }

        super.clear();
    }

    final O getArray() {
        return array;
    }

    final void setArray(O array) {

        this.array = Objects.requireNonNull(array);
    }

    final I checkCapacity() {

        final int numOuterAllocatedEntries = getNumOuterAllocatedEntries();
        final int numOuterUtilizedEntries = getNumOuterEntries();

        final I result;

        if (numOuterAllocatedEntries == 0) {

            incrementNumOuterAllocatedEntries();
            incrementNumOuterEntries();

            this.array = createOuterArray.apply(1);

            result = setInnerArray(array, 0, getInnerCapacity() + 1);
        }
        else if (numOuterUtilizedEntries == 0) {

            incrementNumOuterEntries();

            result = getInnerArray(array, 0);

            setInnerArrayLength(result, 0);
        }
        else {
            final I innerArray = getInnerArray(array, numOuterUtilizedEntries - 1);

            final int numInnerElements = getNumInnerElements(innerArray);

            if (numInnerElements == getInnerArrayLength(innerArray) - 1) {

                final int outerArrayLength = getOuterArrayLength(array);

                if (numOuterUtilizedEntries < numOuterAllocatedEntries) {

                    result = getInnerArray(array, numOuterUtilizedEntries);
                }
                else {
                    if (numOuterUtilizedEntries == outerArrayLength) {

                        this.array = copyOuterArray(array, outerArrayLength << 2);

                        setArray(array);
                    }

                    incrementNumOuterAllocatedEntries();

                    result = setInnerArray(array, numOuterUtilizedEntries, getInnerNumAllocateElements());

                    setInnerArrayLength(result, 0);
                }

                incrementNumOuterEntries();
            }
            else {
                result = innerArray;
            }
        }

        return result;
    }
}
