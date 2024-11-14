package dev.jdata.db.data.locktable;

import java.util.Arrays;

public final class IntLargeArray extends LargeArray<int[][], int[]> {

    public IntLargeArray(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, int[][]::new);
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
