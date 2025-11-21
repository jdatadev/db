package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongArrayTest extends BaseIntCapacityArrayTest<MutableLongArray> {

    @Override
    MutableLongArray createArray(int capacity) {

        return new HeapMutableLongArray(AllocationType.HEAP, capacity);
    }

    @Override
    MutableLongArray createClearArray(int initialCapacity, int clearValue) {

        return new HeapMutableLongArray(AllocationType.HEAP, initialCapacity, clearValue);
    }

    @Override
    int getValue(MutableLongArray array, long index) {

        final long result = array.get(IByIndexView.intIndex(index));

        return Integers.checkLongToInt(result);
    }

    @Override
    void addValue(MutableLongArray array, int value) {

        array.add(value);
    }

    @Override
    void setValue(MutableLongArray array, long index, int value) {

        Objects.requireNonNull(array);
        Checks.isIntIndexNotOutOfBounds(index);

        array.set(IByIndexView.intIndex(index), value);
    }
}
