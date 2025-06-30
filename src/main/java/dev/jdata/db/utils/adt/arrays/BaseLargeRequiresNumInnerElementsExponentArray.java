package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

@Deprecated
abstract class BaseLargeRequiresNumInnerElementsExponentArray<O, I> extends LargeExponentArray<O, I> {

    BaseLargeRequiresNumInnerElementsExponentArray(int initialOuterCapacity, IntFunction<O> createOuterArray, ToIntFunction<O> getOuterArrayLength, int innerCapacityExponent,
            int numInitialOuterAllocatedInnerArrays) {
        super(initialOuterCapacity, createOuterArray, getOuterArrayLength, innerCapacityExponent, numInitialOuterAllocatedInnerArrays, true);
    }
}
