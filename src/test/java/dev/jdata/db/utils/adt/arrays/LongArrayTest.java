package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class LongArrayTest extends BaseIntCapacityArrayTest<LongArray> {

    @Override
    LongArray createArray(int capacity) {

        return new LongArray(capacity);
    }

    @Override
    int getValue(LongArray array, long index) {

        final long result = array.get(Integers.checkUnsignedLongToUnsignedInt(index));

        return Integers.checkLongToInt(result);
    }

    @Override
    void addValue(LongArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(LongArray array, long index, int value) {

        Objects.requireNonNull(array);
        Checks.isIndexNotOutOfBounds(index);

        array.set(Integers.checkUnsignedLongToUnsignedInt(index), value);
    }
}
