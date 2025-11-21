package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableIntArrayTest extends BaseIntCapacityArrayTest<MutableIntArray> {

    @Override
    MutableIntArray createArray(int capacity) {

        return new HeapMutableIntArray(AllocationType.HEAP, capacity);
    }

    @Override
    MutableIntArray createClearArray(int initialCapacity, int clearValue) {

        return new HeapMutableIntArray(AllocationType.HEAP, initialCapacity, clearValue);
    }

    @Override
    int getValue(MutableIntArray array, long index) {

        final long result = array.get(IByIndexView.intIndex(index));

        return Integers.checkLongToInt(result);
    }

    @Override
    void addValue(MutableIntArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(MutableIntArray array, long index, int value) {

        Objects.requireNonNull(array);
        Checks.isIntIndexNotOutOfBounds(index);

        array.set(IByIndexView.intIndex(index), value);
    }
}
