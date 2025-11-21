package dev.jdata.db.utils.adt.arrays;

public final class MutableObjectLargeLargeArrayTest extends BaseLargeArrayTest<Integer[][], Integer[], MutableObjectLargeArray<Integer>> {

    @Override
    MutableObjectLargeArray<Integer> createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new MutableObjectLargeArray<>(initialOuterCapacity, innerCapacityExponent, Integer[][]::new, Integer[]::new);
    }

    @Override
    MutableObjectLargeArray<Integer> createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new MutableObjectLargeArray<>(initialOuterCapacity, innerCapacityExponent, Integer[][]::new, Integer[]::new, clearValue);
    }

    @Override
    int getValue(MutableObjectLargeArray<Integer> array, long index) {

        return array.get(index);
    }

    @Override
    void addValue(MutableObjectLargeArray<Integer> array, int value) {

        array.add(value);
    }

    @Override
    void setValue(MutableObjectLargeArray<Integer> array, long index, int value) {

        array.set(index, value);
    }
}
