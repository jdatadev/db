package dev.jdata.db.data.locktable;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.Elements;
import dev.jdata.db.utils.checks.Checks;

public abstract class LargeArray<O, I> implements Elements {

    private final int innerCapacity;

    private O array;
    private int numOuterEntries;

    private long numElements;

    abstract O copyOuterArray(O outerArray, int capacity);
    abstract int getOuterArrayLength(O outerArray);
    abstract I getInnerArray(O outerArray, int index);
    abstract int getInnerArrayLength(I innerArray);
    abstract void setInnerArrayLength(I innerArray, int length);
    abstract int getNumInnerElements(I innerArray);
    abstract I setInnerArray(O outerArray, int outerIndex, int innerArrayLength);

    protected LargeArray(int initialOuterCapacity, int innerCapacity, IntFunction<O> createOuterArray) {

        Checks.isCapacity(initialOuterCapacity);
        Checks.isCapacity(innerCapacity);
        Objects.requireNonNull(createOuterArray);

        this.innerCapacity = innerCapacity;

        this.array = createOuterArray.apply(initialOuterCapacity);

        this.numElements = 0;
    }

    @Override
    public final boolean isEmpty() {

        return numElements == 0;
    }

    @Override
    public final long getNumElements() {

        return numElements;
    }

    final int getInnerCapacity() {
        return innerCapacity;
    }

    final void increaseNumElements() {

        ++ numElements;
    }

    final int getNumOuterEntries() {
        return numOuterEntries;
    }

    final void increaseNumOuterEntries() {

        ++ numOuterEntries;
    }

    final O getArray() {
        return array;
    }

    final void setArray(O array) {

        this.array = Objects.requireNonNull(array);
    }

    final I checkCapacity() {

        final int numOuterEntries = getNumOuterEntries();

        final I result;

        if (numOuterEntries == 0) {

            increaseNumElements();

            result = setInnerArray(array, 0, getInnerCapacity());
        }
        else {
            final I innerArray = getInnerArray(array, numOuterEntries - 1);

            final int numInnerElements = getNumInnerElements(innerArray);

            if (numInnerElements == getInnerArrayLength(innerArray) - 1) {

                final int outerArrayLength = getOuterArrayLength(array);

                if (numOuterEntries == outerArrayLength) {

                    array = copyOuterArray(array, outerArrayLength * 4);

                    setArray(array);

                    result = getInnerArray(array, numOuterEntries);

                    setInnerArrayLength(result, 0);
//                    result[0] = 0L;

                    increaseNumOuterEntries();
                }
                else {
                    result = innerArray;
                }
            }
            else {
                result = innerArray;
            }
        }

        return result;
    }

    static int getOuterIndex(long index) {

        return (int)(index >>> 32);
    }

    static int getInnerIndex(long index) {

        return (int)(index & 0xFFFFFFFFFL);
    }
}
