package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

public final class LargeIntArray extends LargeLimitArray<int[][], int[]> {

    public LargeIntArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent, 0, int[][]::new);
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    public int get(long index) {

        return getArray()[getOuterIndex(index)][getInnerIndexNotCountingNumElements(index) + 1];
    }

    public void add(int value) {

        final int[] array = checkCapacity();

        final long index = getAndIncrementLimit();

        array[getInnerIndex(index)] = value;
    }

    public void set(long index, int value) {

        Checks.isIndex(index);

        final int outerIndex = ensureCapacityAndLimit(index);

        getArray()[outerIndex][getInnerIndex(index)] = value;
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

    private int[] checkCapacity() {

        return checkCapacity(null, null);
    }

    private int ensureCapacityAndLimit(long index) {

        return ensureCapacityAndLimit(index, null, null);
    }
}
