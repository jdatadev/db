package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.scalars.Integers;

public final class LargeLongArrayTest extends BaseLargeArrayTest<long[][], long[], LargeLongArray> {

    @Override
    LargeLongArray createArray(int initialOuterCapacity, int innerCapacityExponent) {

        return new LargeLongArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    LargeLongArray createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return new LargeLongArray(initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    @Override
    int getValue(LargeLongArray array, long index) {

        return Integers.checkUnsignedLongToUnsignedInt(array.get(index));
    }

    @Override
    void addValue(LargeLongArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(LargeLongArray array, long index, int value) {

        array.set(index, value);
    }
}
