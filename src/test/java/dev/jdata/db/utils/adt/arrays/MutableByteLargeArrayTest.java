package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableByteLargeArrayTest extends BaseLargeArrayTest<byte[][], byte[], MutableByteLargeArray> {

    @Override
    MutableByteLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new HeapMutableByteLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    MutableByteLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new HeapMutableByteLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, Integers.checkIntToByte(clearValue));
    }

    @Override
    int getValue(MutableByteLargeArray array, long index) {

        return array.get(index);
    }

    @Override
    void addValue(MutableByteLargeArray array, int value) {

        array.add(Integers.checkIntToByte(value));
    }

    @Override
    void setValue(MutableByteLargeArray array, long index, int value) {

        array.set(index, Integers.checkIntToByte(value));
    }

    @Override
    int getNumIterations() {

        return Byte.MAX_VALUE / 2;
    }

    @Override
    int getOffset() {

        return Byte.MAX_VALUE / 2;
    }
}
