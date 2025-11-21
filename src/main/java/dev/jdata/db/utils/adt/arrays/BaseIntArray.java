package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;

abstract class BaseIntArray extends BaseIntegerArray<int[]> implements IIntArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_ARRAY;

    BaseIntArray(AllocationType allocationType, int initialCapacity, boolean hasClearValue) {
        super(allocationType, new int[initialCapacity], 0, initialCapacity, hasClearValue, int[]::new);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("hasClearValue", hasClearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntArray(AllocationType allocationType, int[] elements, int limit, boolean hasClearValue) {
        super(allocationType, elements, limit, elements.length, hasClearValue, int[]::new);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("elements", elements).add("limit", limit).add("hasClearValue", hasClearValue));
        }

        setElementsArray(Array.copyOf(elements));

        if (DEBUG) {

            exit();
        }
    }

    BaseIntArray(AllocationType allocationType, BaseIntArray toCopy) {
        super(allocationType, toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        setElementsArray(Array.copyOf(toCopy.getElementsArray()));

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final int get(long index) {

        return getElementsArray()[intIndex(index)];
    }

    @Override
    protected final int[] copyValues(int[] values, long startIndex, long numElements) {

        checkIntCopyValuesParameters(values, values.length, startIndex, numElements);

        return Arrays.copyOfRange(values, intIndex(startIndex), intIndex(startIndex + numElements));
    }
}
