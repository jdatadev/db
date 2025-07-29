package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongArrayTest extends BaseIntCapacityArrayTest<MutableLongArray> {

    @Override
    MutableLongArray createArray(int capacity) {

        return new MutableLongArray(capacity);
    }

    @Override
    MutableLongArray createClearArray(int initialCapacity, int clearValue) {

        return new MutableLongArray(initialCapacity, clearValue);
    }

    @Override
    int getValue(MutableLongArray array, long index) {

        final long result = array.get(Integers.checkUnsignedLongToUnsignedInt(index));

        return Integers.checkLongToInt(result);
    }

    @Override
    void addValue(MutableLongArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(MutableLongArray array, long index, int value) {

        Objects.requireNonNull(array);
        Checks.isIndexNotOutOfBounds(index);

        array.set(Integers.checkUnsignedLongToUnsignedInt(index), value);
    }
}
