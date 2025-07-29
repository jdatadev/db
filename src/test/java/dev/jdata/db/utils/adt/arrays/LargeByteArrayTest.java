package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.scalars.Integers;

public final class LargeByteArrayTest extends BaseLargeArrayTest<byte[][], byte[], LargeByteArray> {

    @Override
    LargeByteArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new LargeByteArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    LargeByteArray createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new LargeByteArray(initialOuterCapacity, innerCapacityExponent, Integers.checkIntToByte(clearValue));
    }

    @Override
    int getValue(LargeByteArray array, long index) {

        return array.get(index);
    }

    @Override
    void addValue(LargeByteArray array, int value) {

        array.add(Integers.checkIntToByte(value));
    }

    @Override
    void setValue(LargeByteArray array, long index, int value) {

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
