package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;

abstract class BaseIntArray extends BaseIntegerArray<int[]> implements IIntArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_ARRAY;

    BaseIntArray(int initialCapacity, boolean hasClearValue) {
        super(new int[initialCapacity], 0, initialCapacity, hasClearValue);

        if (DEBUG) {

            enter(b -> b.add("initialCapacity", initialCapacity).add("hasClearValue", hasClearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntArray(int[] elements, int limit, boolean hasClearValue) {
        super(elements, limit, elements.length, hasClearValue);

        if (DEBUG) {

            enter(b -> b.add("elements", elements).add("limit", limit).add("hasClearValue", hasClearValue));
        }

        this.elements = Array.copyOf(elements);

        if (DEBUG) {

            exit();
        }
    }

    BaseIntArray(BaseIntArray toCopy) {
        super(toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy));
        }

        this.elements = Array.copyOf(toCopy.elements);

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final int get(long index) {

        return elements[IByIndexView.intIndex(index)];
    }
}
