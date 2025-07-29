package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.IClearable;

abstract class BaseIntCapacityArrayTest<T extends IOneDimensionalArrayCommon & IClearable> extends BaseArrayTest<T> {

    private static final int INITIAL_CAPACITY = 1;

    abstract T createArray(int initialCapacity);
    abstract T createClearArray(int initialCapacity, int clearValue);

    @Override
    final T createArray() {

        return createArray(INITIAL_CAPACITY);
    }

    @Override
    final T createArray(int initialOuterCapacity, int initialInnerCapacityExponent) {

        final int initialCapacity = computeCapacity(initialOuterCapacity, initialInnerCapacityExponent);

        return createArray(initialCapacity);
    }

    @Override
    final T createClearArray(int clearValue) {

        return createClearArray(INITIAL_CAPACITY, clearValue);
    }

    @Override
    final T createClearArray(int initialOuterCapacity, int initialInnerCapacityExponent, int clearValue) {

        final int initialCapacity = computeCapacity(initialOuterCapacity, initialInnerCapacityExponent);

        return createClearArray(initialCapacity, clearValue);
    }

    private static int computeCapacity(int outerCapacity, int innerCapacityExponent) {

        return outerCapacity * (1 << innerCapacityExponent);
    }
}
