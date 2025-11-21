package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

public abstract class LargeArray<O, I> extends ExponentLargeArray<O, I> {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_ARRAY;

    protected LargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, boolean hasClearValue, IntFunction<O> createOuterArray) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, 0, hasClearValue, createOuterArray, false);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("hasClearValue", hasClearValue)
                    .add("createOuterArray", createOuterArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    LargeArray(AllocationType allocationType, LargeArray<O, I> toCopy, IOuterAndInnerArraysCopier<O> copyOuterAndInnerArrays) {
        super(allocationType, toCopy, copyOuterAndInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyOuterAndInnerArrays", copyOuterAndInnerArrays));
        }

        if (DEBUG) {

            exit();
        }
    }
}
