package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

public abstract class LargeArray<O, I> extends LargeExponentArray<O, I> {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_ARRAY;

    protected LargeArray(int initialOuterCapacity, int innerCapacityExponent, boolean hasClearValue, IntFunction<O> createOuterArray) {
        super(initialOuterCapacity, innerCapacityExponent, 0, hasClearValue, createOuterArray, false);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("hasClearValue", hasClearValue)
                    .add("createOuterArray", createOuterArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    LargeArray(LargeArray<O, I> toCopy, IOuterAndInnerArraysCopier<O> copyOuterAndInnerArrays) {
        super(toCopy, copyOuterAndInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyOuterAndInnerArrays", copyOuterAndInnerArrays));
        }

        if (DEBUG) {

            exit();
        }
    }
}
