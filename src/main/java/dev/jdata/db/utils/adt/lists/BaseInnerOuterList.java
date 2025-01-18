package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseInnerOuterList<T, V extends BaseValues<T, BaseInnerOuterList<T, V>, V>> extends BaseList<T, BaseInnerOuterList<T, V>, V> {

    private final int outerShift;

    private final long innerMask;

    BaseInnerOuterList(V values, int outerShift, long innerMask) {
        super(values);

        this.outerShift = Checks.isNotNegative(outerShift);
        this.innerMask = innerMask;
    }

    final int getOuterIndex(long node) {

        return (int)(node >>> outerShift);
    }

    final int getInnerIndex(long node) {

        return (int)(node & innerMask);
    }
}
