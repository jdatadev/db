package dev.jdata.db.utils.adt.arrays;

public final class IntLargeArrayTest extends BaseLargeArrayTest<int[][], int[], IntLargeArray> {

    @Override
    IntLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new IntLargeArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    int getValue(IntLargeArray array, long index) {

        return array.get(index);
    }

    @Override
    void addValue(IntLargeArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(IntLargeArray array, long index, int value) {

        array.set(index, value);
    }
}
