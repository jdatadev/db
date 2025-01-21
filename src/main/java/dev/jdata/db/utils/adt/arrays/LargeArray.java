package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.Clearable;
import dev.jdata.db.utils.adt.elements.Elements;
import dev.jdata.db.utils.checks.Checks;

public abstract class LargeArray<O, I> implements Elements, Clearable {

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

        this.numElements = 0L;
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
    public final void clear() {

        final int numOuter = numOuterEntries;

        for (int i = 0; i < numOuter; ++ i) {

            setInnerArrayLength(getInnerArray(array, i), 0);
        }

        this.numElements = 0L;
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

            result = setInnerArray(array, 0, getInnerCapacity() + 1);
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
