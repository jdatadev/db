package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableLongLargeArrayTest extends BaseLargeArrayTest<long[][], long[], MutableLongLargeArray> {

    @Override
    MutableLongLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new HeapMutableLongLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    MutableLongLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new HeapMutableLongLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    @Override
    int getValue(MutableLongLargeArray array, long index) {

        return IByIndexView.intIndex(array.get(index));
    }

    @Override
    void addValue(MutableLongLargeArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(MutableLongLargeArray array, long index, int value) {

        array.set(index, value);
    }
}
