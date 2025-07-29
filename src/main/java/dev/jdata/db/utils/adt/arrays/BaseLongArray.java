package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.scalars.Integers;

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

        return elements[Integers.checkUnsignedLongToUnsignedInt(index)];
    }

    @Override
    public final void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [elements=" + Arrays.toString(elements) + "]";
    }
}
