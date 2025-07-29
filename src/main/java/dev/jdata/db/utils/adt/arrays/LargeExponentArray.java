package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;
import java.util.function.ObjLongConsumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.byindex.IByIndex;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class LargeExponentArray<O, I> extends BaseLargeOneDimensionalArray<O, I> implements IByIndex {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_EXPONENT_ARRAY;

    private final int outerIndexShift;
    private final int innerElementCapacityExponent;
    private final long innerIndexMask;

    LargeExponentArray(int initialOuterCapacity, int innerCapacityExponent, int numInitialOuterAllocatedInnerArrays, boolean hasClearValue, IntFunction<O> createOuterArray,
            boolean requiresInnerArrayNumElements) {
        super(initialOuterCapacity, CapacityExponents.computeLongCapacityFromExponent(innerCapacityExponent), numInitialOuterAllocatedInnerArrays, hasClearValue,
                createOuterArray, requiresInnerArrayNumElements);

        Checks.isIntCapacityExponent(innerCapacityExponent);
        Checks.isNotNegative(numInitialOuterAllocatedInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("numInitialOuterAllocatedInnerArrays", numInitialOuterAllocatedInnerArrays).add("hasClearValue", hasClearValue)
                    .add("createOuterArray", createOuterArray).add("requiresInnerArrayNumElements", requiresInnerArrayNumElements));
        }

        this.innerElementCapacityExponent = innerCapacityExponent;

        final int innerCapacityNumBits = innerCapacityExponent;

        this.outerIndexShift = innerCapacityNumBits;
        this.innerIndexMask = BitsUtil.maskLong(innerCapacityNumBits);

        if (DEBUG) {

            exit();
        }
    }

    LargeExponentArray(LargeExponentArray<O, I> toCopy, IOuterAndInnerArraysCopier<O> copyOuterAndInnerArrays) {
        super(toCopy, copyOuterAndInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyOuterAndInnerArrays", copyOuterAndInnerArrays));
        }

        this.outerIndexShift = toCopy.outerIndexShift;
        this.innerElementCapacityExponent = toCopy.innerElementCapacityExponent;
        this.innerIndexMask = toCopy.innerIndexMask;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final long getArrayElementCapacity() {

        return computeElementCapacity(getOuterArrayCapacity());
    }

    @Override
    protected final int getOuterIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index >>> outerIndexShift);
    }

    protected final long getInnerElementCapacityExponent() {
        return innerElementCapacityExponent;
    }

    protected final long getInnerIndexMask() {
        return innerIndexMask;
    }

    @Override
    protected final <INCREASE_LIMIT_PARAMETER> int ensureCapacityAndLimitAndReturnOuterIndex(long index, long limit, INCREASE_LIMIT_PARAMETER increaseLimitParameter,
            ObjLongConsumer<INCREASE_LIMIT_PARAMETER> limitIncreaser, boolean clearInnerArrays) {

        if (DEBUG) {

            enter(b -> b.add("index", index).add("limit", limit).add("increaseLimitParameter", increaseLimitParameter).add("limitIncreaser", limitIncreaser)
                    .add("clearInnerArrays", clearInnerArrays));
        }

        if (index >= limit) {

            final long newLimit = index + 1;

            ensureCapacityAndLimitWithNewLimit(limit, newLimit, clearInnerArrays);

            if (limitIncreaser != null) {

                limitIncreaser.accept(increaseLimitParameter, newLimit - limit);
            }
        }

        final int result = getOuterIndex(index);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final <INCREASE_LIMIT_PARAMETER> void ensureCapacityAndLimitWithNewLimit(long limit, long newLimit, boolean clearInnerArrays) {

        Checks.isArrayLimit(limit);
        Checks.isGreaterThan(newLimit, limit);

        if (DEBUG) {

            enter(b -> b.add("limit", limit).add("newLimit", newLimit).add("clearInnerArrays", clearInnerArrays));
        }

        final long allocatedInnerArrayElementsCapacity = computeElementCapacity(getNumOuterAllocatedInnerArrays());

        if (newLimit > allocatedInnerArrayElementsCapacity) {

            checkCapacityWithNewLimit(limit, newLimit, clearInnerArrays);
        }
        else {
            final int numOuter = getNumOuterUtilizedEntries();

            final int requiredNumOuter = computeRequiredElementCapacity(newLimit);

            if (requiredNumOuter > numOuter) {

                setNumOuterUtilizedEntries(requiredNumOuter, getNumOuterAllocatedInnerArrays());
            }
        }
/*
        final int outerIndex = getOuterIndex(index);

        if (DEBUG) {

            exit(outerIndex);
        }

        return outerIndex;
*/
        if (DEBUG) {

            exit();
        }
    }

    protected final int getInnerElementIndex(long index) {

        return (int)(index & innerIndexMask);
    }

    private int computeRequiredElementCapacity(long limit) {

        final int exponent = innerElementCapacityExponent;

        final int numOuter = Integers.checkUnsignedLongToUnsignedInt(limit >> exponent);

        return limit == numOuter << exponent ? numOuter : numOuter + 1;
    }

    private long computeElementCapacity(int numOuter) {

        return numOuter << innerElementCapacityExponent;
    }
}
