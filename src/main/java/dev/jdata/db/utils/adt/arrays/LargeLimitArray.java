package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.checks.Checks;

abstract class LargeLimitArray<O, I> extends LargeArray<O, I> implements IArray {

    private long limit;

    LargeLimitArray(int initialOuterCapacity, IntFunction<O> createOuterArray, ToIntFunction<O> getOuterArrayLength, int innerCapacityExponent) {
        super(initialOuterCapacity, createOuterArray, getOuterArrayLength, innerCapacityExponent);

        this.limit = 0L;
    }

    @Override
    public void clear() {

        super.clear();

        this.limit = 0L;
    }

    @Override
    public final long getLimit() {

        return limit;
    }

    @Override
    final long getRemainderOfLastInnerArray(int outerIndex) {

        return getRemainderOfLastInnerArrayWithLimit(outerIndex, limit);
    }

    final long getAndIncrementLimit() {

        return this.limit ++;
    }

    int ensureCapacityAndLimit(long index) {

        return ensureCapacityAndLimit(index, null, null);
    }

    final <P> int ensureCapacityAndLimit(long index, P parameter, ArrayClearer<I, P> arrayClearer) {

        Checks.isIndex(index);

        return ensureCapacityAndLimit(index, getLimit(), this, parameter, (t, i) -> t.increaseLimit(i), arrayClearer);
    }

    final void increaseLimit(long numElements) {

        Checks.isLengthAboveZero(numElements);

        this.limit += numElements;
    }
}
