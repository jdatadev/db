package dev.jdata.db.data.locktable;

import java.util.Arrays;

public final class LongLargeArray extends LargeArray<long[][], long[]> {

    LongLargeArray(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, long[][]::new);
    }

    public long get(long index) {

        return getArray()[getOuterIndex(index)][getInnerIndex(index)];
    }

    public void set(long index, long value) {

        final int outerIndex = getOuterIndex(index);

        if (outerIndex >= getNumOuterEntries()) {

            throw new IllegalArgumentException();
        }

        final long[] array = getArray()[outerIndex];

        final int numInnerElements = (int)array[0];

        final int innerIndex = getInnerIndex(index);

        if (innerIndex >= numInnerElements) {

            throw new IllegalArgumentException();
        }

        array[innerIndex + 1] = value;
    }

    public void add(long value) {

        final long[] array = checkCapacity();

        increaseNumElements();

        final int numInnerElements = (int)array[0];

        final int innerIndex = (int)array[0];

        ++ array[0];

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
