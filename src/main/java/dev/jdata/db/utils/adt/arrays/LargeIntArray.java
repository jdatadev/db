package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

public final class LargeIntArray extends LargeLimitArray<int[][], int[]> {

    public LargeIntArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, int[][]::new, a -> a.length, innerCapacityExponent);
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    public int get(long index) {

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    public void add(int value) {

        final int[] array = checkCapacity();

        final long index = getAndIncrementLimit();

        array[getInnerElementIndex(index)] = value;
    }

    public void set(long index, int value) {

        Checks.isIndexNotOutOfBounds(index);

        final int outerIndex = ensureCapacityAndLimit(index);

        getOuterArray()[outerIndex][getInnerElementIndex(index)] = value;
    }

    @Override
    protected int[][] copyOuterArray(int[][] outerArray, int capacity) {

        return Arrays.copyOf(outerArray, capacity);
    }

    @Override
    protected int getOuterArrayLength(int[][] outerArray) {

        return outerArray.length;
    }

    @Override
    protected int[] getInnerArray(int[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    protected int[] abstractCreateAndSetInnerArray(int[][] outerArray, int outerIndex, int innerArrayLength) {

        return outerArray[outerIndex] = new int[innerArrayLength];
    }
}
