package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseInnerOuterNodeList<

                TO_ARRAY,
                VALUES_LIST extends IInnerOuterNodeListInternal<TO_ARRAY>,
                VALUES extends BaseInnerOuterNodeListValues<TO_ARRAY, VALUES_LIST>>

        extends BaseNodeList<TO_ARRAY, VALUES_LIST, VALUES>
        implements IInnerOuterNodeListInternal<TO_ARRAY> {

    private final int outerShift;
    private final long innerMask;

    BaseInnerOuterNodeList(AllocationType allocationType, VALUES values, int numInnerBits) {
        super(allocationType, values);

        this.outerShift = Checks.isNotNegative(numInnerBits);
        this.innerMask = BitsUtil.maskLong(numInnerBits);
    }

    @Override
    public final int getOuterIndex(long node) {

        return (int)(node >>> outerShift);
    }

    @Override
    public final int getInnerIndex(long node) {

        return (int)(node & innerMask);
    }
}
