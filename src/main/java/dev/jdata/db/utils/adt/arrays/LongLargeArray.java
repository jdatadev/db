package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

public final class LongLargeArray extends LargeArray<long[][], long[]> implements LargeLongArray {

    public LongLargeArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent, 1, long[][]::new);
    }

    @Override
    protected void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public long get(long index) {

        return getArray()[getOuterIndex(index)][getInnerIndexNotCountingNumElements(index) + 1];
    }

    public void add(long value) {

        final long[] array = checkCapacity();

        incrementNumElements();

        final int innerIndex = (int)array[0];

        ++ array[0];

        array[innerIndex + 1] = value;
    }

    public void set(long index, long value) {

        Objects.checkIndex(index, getNumElements());

        final int outerIndex = getOuterIndex(index);

        if (outerIndex >= getNumOuterEntries()) {

            throw new IllegalArgumentException();
        }

        final long[] array = getArray()[outerIndex];

        final int numInnerElements = getNumInnerElements(array);

        final int innerIndex = getInnerIndexNotCountingNumElements(index);

        if (innerIndex >= numInnerElements) {

            throw new IllegalArgumentException();
        }

        array[innerIndex + 1] = value;
    }

    @Override
    long[][] copyOuterArray(long[][] outerArray, int capacity) {

        return Arrays.copyOf(outerArray, capacity);
    }

    @Override
    int getOuterArrayLength(long[][] outerArray) {

        return outerArray.length;
    }

    @Override
    long[] getInnerArray(long[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    int getInnerArrayLength(long[] innerArray) {

        return innerArray.length;
    }

    @Override
    void setInnerArrayLength(long[] innerArray, int length) {

        innerArray[0] = length;
    }

    @Override
    int getNumInnerElements(long[] innerArray) {

        return (int)innerArray[0];
    }

    @Override
    long[] setInnerArray(long[][] outerArray, int outerIndex, int innerArrayLength) {

        return outerArray[outerIndex] = new long[innerArrayLength];
    }
}
