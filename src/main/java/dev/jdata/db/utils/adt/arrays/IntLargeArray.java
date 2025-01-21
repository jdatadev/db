package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

public final class IntLargeArray extends LargeArray<int[][], int[]> {

    public IntLargeArray(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, int[][]::new);
    }

    public int get(long index) {

        return getArray()[getOuterIndex(index)][getInnerIndex(index)];
    }

    public void set(long index, int value) {

        final int outerIndex = getOuterIndex(index);

        if (outerIndex >= getNumOuterEntries()) {

            throw new IllegalArgumentException();
        }

        final int[] array = getArray()[outerIndex];

        final int numInnerElements = array[0];

        final int innerIndex = getInnerIndex(index);

        if (innerIndex >= numInnerElements) {

            throw new IllegalArgumentException();
        }

        array[innerIndex + 1] = value;
    }

    public void add(int value) {

        final int[] array = checkCapacity();

        increaseNumElements();

        final int numInnerElements = array[0];

        final int innerIndex = array[0];

        ++ array[0];

        if (innerIndex >= numInnerElements) {

            throw new IllegalArgumentException();
        }

        array[innerIndex + 1] = value;
    }

    @Override
    int[][] copyOuterArray(int[][] outerArray, int capacity) {

        return Arrays.copyOf(outerArray, capacity);
    }

    @Override
    int getOuterArrayLength(int[][] outerArray) {

        return outerArray.length;
    }

    @Override
    int[] getInnerArray(int[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    int getInnerArrayLength(int[] innerArray) {

        return innerArray.length;
    }

    @Override
    void setInnerArrayLength(int[] innerArray, int length) {

        innerArray[0] = 0;
    }

    @Override
    int getNumInnerElements(int[] innerArray) {

        return innerArray[0];
    }

    @Override
    int[] setInnerArray(int[][] outerArray, int outerIndex, int innerArrayLength) {

        return outerArray[outerIndex] = new int[innerArrayLength];
    }
}
