package dev.jdata.db.utils.adt.arrays;

public final class LargeIntArrayTest extends BaseLargeArrayTest<int[][], int[], LargeIntArray> {

    @Override
    LargeIntArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new LargeIntArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    LargeIntArray createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new LargeIntArray(initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    @Override
    int getValue(LargeIntArray array, long index) {

        return array.get(index);
    }

    @Override
    void addValue(LargeIntArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(LargeIntArray array, long index, int value) {

        array.set(index, value);
    }
}
