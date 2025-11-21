package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;

abstract class BaseLongArray extends BaseIntegerArray<long[]> implements ILongArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_ARRAY;

    BaseLongArray(long[] elements, int limit, boolean hasClearValue) {
        super(elements, limit, elements.length, hasClearValue);

        if (DEBUG) {

            enter(b -> b.add("elements", elements).add("limit", limit).add("hasClearValue", hasClearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongArray(BaseLongArray toCopy) {
        super(toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long get(long index) {

        return getElements()[IByIndexView.intIndex(index)];
    }

    @Override
    public <P> boolean contains(P parameter, ILongElementPredicate<P> predicate) {
        // TODO Auto-generated method stub
        return false;
    }
}
