package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableIntArrayTest extends BaseIntCapacityArrayTest<MutableIntArray> {

    @Override
    MutableIntArray createArray(int capacity) {

        return new MutableIntArray(capacity);
    }

    @Override
    MutableIntArray createClearArray(int initialCapacity, int clearValue) {

        return new MutableIntArray(initialCapacity, clearValue);
    }

    @Override
    int getValue(MutableIntArray array, long index) {

        final long result = array.get(Integers.checkUnsignedLongToUnsignedInt(index));

        return Integers.checkLongToInt(result);
    }

    @Override
    void addValue(MutableIntArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(MutableIntArray array, long index, int value) {

        Objects.requireNonNull(array);
        Checks.isIndexNotOutOfBounds(index);

        array.set(Integers.checkUnsignedLongToUnsignedInt(index), value);
    }
}
