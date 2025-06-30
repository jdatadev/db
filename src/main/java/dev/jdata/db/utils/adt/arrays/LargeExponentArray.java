package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.byindex.IByIndex;
import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class LargeExponentArray<O, I> extends BaseLargeOneDimensionalArray<O, I> implements ICapacity, IByIndex {

    private final int outerIndexShift;
    private final long innerIndexMask;

    LargeExponentArray(int initialOuterCapacity, IntFunction<O> createOuterArray, ToIntFunction<O> getOuterArrayLength, int innerCapacityExponent,
            int numInitialOuterAllocatedInnerArrays, boolean requiresInnerArrayNumElements) {
        super(initialOuterCapacity, CapacityExponents.computeCapacity(innerCapacityExponent), numInitialOuterAllocatedInnerArrays, createOuterArray, getOuterArrayLength,
                requiresInnerArrayNumElements);

        Checks.isCapacityExponent(innerCapacityExponent);
        Checks.isNotNegative(numInitialOuterAllocatedInnerArrays);

        final int innerCapacityNumBits = innerCapacityExponent;

        this.outerIndexShift = innerCapacityNumBits;
        this.innerIndexMask = BitsUtil.maskLong(innerCapacityNumBits);
    }

    @Override
    protected final int getOuterIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index >>> outerIndexShift);
    }

    protected final long getInnerIndexMask() {
        return innerIndexMask;
    }

    protected final int getInnerElementIndex(long index) {

        return (int)(index & innerIndexMask);
    }
}
