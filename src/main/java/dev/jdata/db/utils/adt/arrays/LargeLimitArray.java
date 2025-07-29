package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;

abstract class LargeLimitArray<O, I> extends LargeArray<O, I> implements IOneDimensionalArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_LIMIT_ARRAY;

    private long limit;

    LargeLimitArray(int initialOuterCapacity, int innerCapacityExponent, boolean hasClearValue, IntFunction<O> createOuterArray) {
        super(initialOuterCapacity, innerCapacityExponent, hasClearValue, createOuterArray);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("hasClearValue", hasClearValue)
                    .add("createOuterArray", createOuterArray));
        }

        this.limit = 0L;

        if (DEBUG) {

            exit();
        }
    }

    LargeLimitArray(LargeLimitArray<O, I> toCopy, IOuterAndInnerArraysCopier<O> copyOuterAndInnerArrays) {
        super(toCopy, copyOuterAndInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyOuterAndInnerArrays", copyOuterAndInnerArrays));
        }

        this.limit = toCopy.limit;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected void clearArray() {

        if (DEBUG) {

            enter();
        }

        super.clearArray();

        this.limit = 0L;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long getLimit() {

        return limit;
    }

    final int ensureCapacityAndLimitAndReturnOuterIndex(long index, boolean clearInnerArrays) {

        Checks.isIndex(index);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("clearInnerArrays", clearInnerArrays));
        }

        final int result = ensureCapacityAndLimitAndReturnOuterIndex(index, getLimit(), this, (t, i) -> t.increaseLimit(i), clearInnerArrays);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final I ensureCapacityAndLimitAndReturnInnerArray(long index, boolean clearInnerArrays) {

        Checks.isIndex(index);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("clearInnerArrays", clearInnerArrays));
        }

        final int outerIndex = ensureCapacityAndLimitAndReturnOuterIndex(index, clearInnerArrays);

        final I result = getInnerArray(outerIndex);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final void incrementLimit() {

        if (DEBUG) {

            enter();
        }

        increaseLimit(1L);

        if (DEBUG) {

            exit();
        }
    }

    final void increaseLimit(long numElements) {

        Checks.isLengthAboveZero(numElements);

        if (DEBUG) {

            enter(b -> b.add("numElements", numElements));
        }

        this.limit += numElements;

        if (DEBUG) {

            exit(limit);
        }
    }
}
