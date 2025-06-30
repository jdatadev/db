package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public abstract class LargeArray<O, I> extends LargeExponentArray<O, I> {

    protected abstract int getOuterArrayLength(O outerArray);

    protected LargeArray(int initialOuterCapacity, IntFunction<O> createOuterArray, ToIntFunction<O> getOuterArrayLength, int innerCapacityExponent) {
        super(initialOuterCapacity, createOuterArray, getOuterArrayLength, innerCapacityExponent, 0, false);
    }
}
