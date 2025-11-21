package dev.jdata.db.utils.adt.arrays;

public final class MutableIntLargeArrayTest extends BaseLargeArrayTest<int[][], int[], MutableIntLargeArray> {

    @Override
    MutableIntLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new MutableIntLargeArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    MutableIntLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new MutableIntLargeArray(initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    @Override
    int getValue(MutableIntLargeArray array, long index) {

        return array.get(index);
    }

    @Override
    void addValue(MutableIntLargeArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(MutableIntLargeArray array, long index, int value) {

        array.set(index, value);
    }
}
