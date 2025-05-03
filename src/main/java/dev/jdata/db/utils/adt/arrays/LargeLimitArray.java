package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

abstract class LargeLimitArray<O, I> extends LargeArray<O, I> implements IArray {

    private long limit;

    LargeLimitArray(int initialOuterCapacity, int innerCapacityExponent, int innerArrayLengthNumElements, IntFunction<O> createOuterArray) {
        super(initialOuterCapacity, innerCapacityExponent, innerArrayLengthNumElements, createOuterArray);

        this.limit = 0L;
    }

    @Override
    public final boolean isEmpty() {

        return limit == 0L;
    }

    @Override
    public final long getLimit() {

        return limit;
    }

    @Override
    public final void reset() {

        this.limit = 0L;

        super.reset();
    }

    final long getAndIncrementLimit() {

        return this.limit ++;
    }

    final <P> int ensureCapacityAndLimit(long index, P parameter, ArrayClearer<I, P> arrayClearer) {

        Checks.isIndex(index);

        final long capacity = getCapacity();

        final long limit = getLimit();

        if (index >= limit) {

            if (index >= capacity) {

                checkCapacity(index - capacity + 1, parameter, arrayClearer);
            }

            increaseLimit(index - limit + 1);
        }

        final int outerIndex = getOuterIndex(index);

        final int numOuterUtilizedEntries = getNumOuterUtilizedEntries();

        if (outerIndex >= numOuterUtilizedEntries) {

            incrementNumOuterUtilizedEntries(outerIndex - numOuterUtilizedEntries + 1);
        }

        return outerIndex;
    }

    final void increaseLimit(long numElements) {

        Checks.isLengthAboveZero(numElements);

        this.limit += numElements;
    }
}
