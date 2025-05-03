package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.byindex.IByIndex;
import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class LargeExponentArray implements ICapacity, IByIndex, IResettable {

    private final int innerCapacityExponent;
    private final int innerArrayLengthNumElements;

    private final int outerIndexShift;
    private final long innerIndexMask;

    private final int innerCapacity;
    private final int innerNumAllocateElements;

    private int numOuterUtilizedEntries;

    private int numOuterAllocatedEntries;

    protected LargeExponentArray(int innerCapacityExponent, int numOuterAllocatedEntries, int innerArrayLengthNumElements) {

        Checks.isCapacityExponent(innerCapacityExponent);
        Checks.isNotNegative(numOuterAllocatedEntries);
        Checks.isNotNegative(innerArrayLengthNumElements);

        this.innerCapacityExponent = innerCapacityExponent;
        this.innerArrayLengthNumElements = innerArrayLengthNumElements;

        final int innerCapacityNumBits = innerCapacityExponent;

        this.outerIndexShift = innerCapacityNumBits;
        this.innerIndexMask = BitsUtil.maskLong(innerCapacityNumBits);

        this.innerCapacity = CapacityExponents.computeCapacity(innerCapacityExponent);

        this.innerNumAllocateElements = innerCapacity + innerArrayLengthNumElements;

        this.numOuterAllocatedEntries = numOuterAllocatedEntries;

        this.numOuterUtilizedEntries = 0;
    }

    @Override
    public void reset() {

        this.numOuterUtilizedEntries = 0;
    }

    @Override
    public final long getCapacity() {

        return numOuterAllocatedEntries * innerCapacity;
    }

    private int getInnerCapacityExponent() {
        return innerCapacityExponent;
    }

    protected final long getInnerIndexMask() {
        return innerIndexMask;
    }

    protected final int getInnerArrayLengthNumElements() {
        return innerArrayLengthNumElements;
    }

    protected final int getInnerNumAllocateElements() {
        return innerNumAllocateElements;
    }

    protected final int getNumOuterAllocatedEntries() {
        return numOuterAllocatedEntries;
    }

    protected final void incrementNumOuterAllocatedEntries(int numAdditional) {

        Checks.isLengthAboveZero(numAdditional);

        this.numOuterAllocatedEntries += numAdditional;
    }

    protected final void incrementNumOuterAllocatedEntries() {

        ++ numOuterAllocatedEntries;
    }

    protected final int getNumOuterUtilizedEntries() {
        return numOuterUtilizedEntries;
    }

    protected final void incrementNumOuterUtilizedEntries(int numAdditional) {

        Checks.isLengthAboveZero(numAdditional);

        this.numOuterUtilizedEntries += numAdditional;
    }

    protected final void incrementNumOuterUtilizedEntries() {

        ++ numOuterUtilizedEntries;
    }

    protected final int getInnerCapacity() {
        return innerCapacity;
    }

    protected final int getOuterIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index >>> outerIndexShift);
    }

    protected final int getInnerIndex(long index) {

        return getInnerIndexNotCountingNumElements(index) + getInnerArrayLengthNumElements();
    }

    protected final int getInnerIndexNotCountingNumElements(long index) {

        return (int)(index & innerIndexMask);
    }
}
