package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

@Deprecated // currently not in use
public final class LargeCharArray extends LargeArray<char[][], char[]> {

    public LargeCharArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent, 1, char[][]::new);
    }

    @Override
    protected void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    public char get(long index) {

        return getArray()[getOuterIndex(index)][getInnerIndexNotCountingNumElements(index) + 1];
    }

    public void add(char value) {

        final char[] array = checkCapacity();

        incrementNumElements();

        final int numInnerElements = getNumInnerElements(array);

        ++ array[0];

        array[numInnerElements + 1] = value;
    }

    public void set(long index, char value) {

        Objects.checkIndex(index, getNumElements());

        final int outerIndex = getOuterIndex(index);

        if (outerIndex >= getNumOuterEntries()) {

            throw new IllegalArgumentException();
        }

        final char[] array = getArray()[outerIndex];

        final int numInnerElements = array[0];

        final int innerIndex = getInnerIndexNotCountingNumElements(index);

        if (innerIndex >= numInnerElements) {

            throw new IllegalArgumentException();
        }

        array[innerIndex + 1] = value;
    }

    @Override
    char[][] copyOuterArray(char[][] outerArray, int capacity) {

        return Arrays.copyOf(outerArray, capacity);
    }

    @Override
    int getOuterArrayLength(char[][] outerArray) {

        return outerArray.length;
    }

    @Override
    char[] getInnerArray(char[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    int getInnerArrayLength(char[] innerArray) {

        return innerArray.length;
    }

    @Override
    void setInnerArrayLength(char[] innerArray, int length) {

        innerArray[0] = 0;
    }

    @Override
    int getNumInnerElements(char[] innerArray) {

        return innerArray[0];
    }

    @Override
    char[] setInnerArray(char[][] outerArray, int outerIndex, int innerArrayLength) {

        return outerArray[outerIndex] = new char[innerArrayLength];
    }
}
