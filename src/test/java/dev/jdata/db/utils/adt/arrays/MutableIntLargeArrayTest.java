package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntLargeArrayTest extends BaseLargeArrayTest<int[][], int[], MutableIntLargeArray> {

    @Override
    MutableIntLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new HeapMutableIntLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    MutableIntLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new HeapMutableIntLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
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
