package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.scalars.Integers;

public final class LongLargeArrayTest extends BaseLargeArrayTest<long[][], long[], LongLargeArray> {

    @Override
    LongLargeArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new LongLargeArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    int getValue(LongLargeArray array, long index) {

        return Integers.checkUnsignedLongToUnsignedInt(array.get(index));
    }

    @Override
    void addValue(LongLargeArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(LongLargeArray array, long index, int value) {

        array.set(index, value);
    }
}
