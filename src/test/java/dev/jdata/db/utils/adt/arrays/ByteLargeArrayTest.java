package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.scalars.Integers;

public final class ByteLargeArrayTest extends BaseLargeArrayTest<byte[][], byte[], ByteLargeArray> {

    @Override
    ByteLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new ByteLargeArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    int getValue(ByteLargeArray array, long index) {

        return array.get(index);
    }

    @Override
    void addValue(ByteLargeArray array, int value) {

        array.add(Integers.checkIntToByte(value));
    }

    @Override
    void setValue(ByteLargeArray array, long index, int value) {

        array.set(index, Integers.checkIntToByte(value));
    }
}
