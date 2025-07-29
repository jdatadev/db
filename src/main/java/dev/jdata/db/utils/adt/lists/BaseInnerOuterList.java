package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseInnerOuterList<
                LIST_T,
                LIST extends BaseInnerOuterList<LIST_T, LIST, VALUES>,
                VALUES extends BaseValues<LIST_T, LIST, VALUES>>

        extends BaseList<LIST_T, LIST, VALUES> {

    private final int outerShift;
    private final long innerMask;

    BaseInnerOuterList(VALUES values, int numInnerBits) {
        super(values);

        this.outerShift = Checks.isNotNegative(numInnerBits);
        this.innerMask = BitsUtil.maskLong(numInnerBits);
    }

    public final int getOuterIndex(long node) {

        return (int)(node >>> outerShift);
    }

    public final int getInnerIndex(long node) {

        return (int)(node & innerMask);
    }
}
