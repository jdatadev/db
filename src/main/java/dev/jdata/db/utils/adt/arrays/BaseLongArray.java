package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;

abstract class BaseLongArray extends BaseIntegerArray<long[]> implements ILongArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_ARRAY;

    BaseLongArray(AllocationType allocationType, long[] elementsArray, int limit, boolean hasClearValue) {
        super(allocationType, elementsArray, limit, elementsArray.length, hasClearValue, long[]::new);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("elementsArray", elementsArray).add("limit", limit).add("hasClearValue", hasClearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongArray(AllocationType allocationType, BaseLongArray toCopy) {
        super(allocationType, toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long get(long index) {

        return getElementsArray()[intIndex(index)];
    }

    @Override
    protected final long[] copyValues(long[] values, long startIndex, long numElements) {

        checkIntCopyValuesParameters(values, values.length, startIndex, numElements);

        return Arrays.copyOfRange(values, intIndex(startIndex), intIndex(startIndex + numElements));
    }
}
