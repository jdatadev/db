package dev.jdata.db.utils.adt.arrays;

public final class LargeObjectArrayTest extends BaseLargeArrayTest<Integer[][], Integer[], LargeObjectArray<Integer>> {

    @Override
    LargeObjectArray<Integer> createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new LargeObjectArray<>(initialOuterCapacity, innerCapacityExponent, Integer[][]::new, Integer[]::new);
    }

    @Override
    LargeObjectArray<Integer> createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new LargeObjectArray<>(initialOuterCapacity, innerCapacityExponent, Integer[][]::new, Integer[]::new, clearValue);
    }

    @Override
    int getValue(LargeObjectArray<Integer> array, long index) {

        return array.get(index);
    }

    @Override
    void addValue(LargeObjectArray<Integer> array, int value) {

        array.add(value);
    }

    @Override
    void setValue(LargeObjectArray<Integer> array, long index, int value) {

        array.set(index, value);
    }
}
