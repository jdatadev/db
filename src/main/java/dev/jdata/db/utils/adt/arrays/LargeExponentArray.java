package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.elements.BaseByIndexElements;
import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class LargeExponentArray extends BaseByIndexElements implements IMutableElements, ICapacity {

    private final int outerIndexShift;
    private final long innerIndexMask;

    private final int innerCapacity;
    private final int innerNumAllocateElements;

    private int numOuterEntries;

    private int numOuterAllocatedEntries;

    protected LargeExponentArray(int innerCapacityExponent, int numOuterAllocatedEntries, int innerArrayLengthNumElements) {

        Checks.isCapacityExponent(innerCapacityExponent);
        Checks.isNotNegative(numOuterAllocatedEntries);
        Checks.isAboveZero(innerArrayLengthNumElements);

        final int innerCapacityNumBits = innerCapacityExponent;

        this.outerIndexShift = innerCapacityNumBits;
        this.innerIndexMask = BitsUtil.maskLong(innerCapacityNumBits);

        this.innerCapacity = CapacityExponents.computeCapacity(innerCapacityExponent);

        this.innerNumAllocateElements = innerCapacity + innerArrayLengthNumElements;

        this.numOuterAllocatedEntries = numOuterAllocatedEntries;

        this.numOuterEntries = 0;
    }

    @Override
    public void clear() {

        super.clearNumElements();

        this.numOuterEntries = 0;
    }

    @Override
    public final long getCapacity() {

        return numOuterAllocatedEntries * innerCapacity;
    }

    protected final int getInnerNumAllocateElements() {
        return innerNumAllocateElements;
    }

    protected final int getNumOuterAllocatedEntries() {
        return numOuterAllocatedEntries;
    }

    protected final void incrementNumOuterAllocatedEntries() {

        ++ numOuterAllocatedEntries;
    }

    protected final int getNumOuterEntries() {
        return numOuterEntries;
    }

    protected final void incrementNumOuterEntries() {

        ++ numOuterEntries;
    }

    protected final int getInnerCapacity() {
        return innerCapacity;
    }

    protected final int getOuterIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index >>> outerIndexShift);
    }

    protected final int getInnerIndexNotCountingNumElements(long index) {

        return (int)(index & innerIndexMask);
    }
}
