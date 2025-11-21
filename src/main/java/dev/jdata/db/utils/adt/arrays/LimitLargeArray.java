package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;

abstract class LimitLargeArray<O, I> extends LargeArray<O, I> implements IOneDimensionalArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_LIMIT_ARRAY;

    private long limit;

    LimitLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, boolean hasClearValue, IntFunction<O> createOuterArray) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, hasClearValue, createOuterArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("hasClearValue", hasClearValue).add("createOuterArray", createOuterArray));
        }

        this.limit = 0L;

        if (DEBUG) {

            exit();
        }
    }

    LimitLargeArray(AllocationType allocationType, LimitLargeArray<O, I> toCopy, IOuterAndInnerArraysCopier<O> copyOuterAndInnerArrays) {
        super(allocationType, toCopy, copyOuterAndInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyOuterAndInnerArrays", copyOuterAndInnerArrays));
        }

        this.limit = toCopy.limit;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final void clearArray() {

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

        Checks.isLongIndex(index);

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

        Checks.isLongIndex(index);

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

        Checks.isLongLengthAboveZero(numElements);

        if (DEBUG) {

            enter(b -> b.add("numElements", numElements));
        }

        this.limit += numElements;

        if (DEBUG) {

            exit(limit);
        }
    }
}
