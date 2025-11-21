package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;

abstract class MutableIntArray extends BaseIntArray implements IMutableIntArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_ARRAY;

    private final int clearValue;

    MutableIntArray(AllocationType allocationType, int initialCapacity) {
        this(allocationType, initialCapacity, 0, false);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntArray(AllocationType allocationType, int initialCapacity, int clearValue) {
        this(allocationType, initialCapacity, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableIntArray(AllocationType allocationType, int initialCapacity, int clearValue, boolean hasClearValue) {
        super(allocationType, createArray(initialCapacity, clearValue, hasClearValue), 0, hasClearValue);

        this.clearValue = clearValue;
    }

    private static int[] createArray(int initialCapacity, int clearValue, boolean hasClearValue) {

        final int[] array = new int[initialCapacity];

        if (hasClearValue) {

            Arrays.fill(array, clearValue);
        }

        return array;
    }

    MutableIntArray(AllocationType allocationType, MutableIntArray toCopy) {
        super(allocationType, toCopy);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        this.clearValue = toCopy.clearValue;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long getCapacity() {

        return getElementsArray().length;
    }

    @Override
    public final void clear() {

        if (DEBUG) {

            enter();
        }

        clearArray();

        if (hasClearValue()) {

            Arrays.fill(getElementsArray(), clearValue);
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final void add(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int index = ensureAddIndex();

        getElementsArray()[index] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final void set(long index, int value) {

        if (DEBUG) {

            enter(b -> b.add("index", index).add("value", value));
        }

        final int intIndex = ensureIndex(index);

        getElementsArray()[intIndex] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    final int[] reallocate(int[] elementsArray, int newCapacity) {

        checkReallocateParameters(elementsArray, elementsArray.length, newCapacity);

        return Arrays.copyOf(elementsArray, newCapacity);
    }

    @Override
    final void clearElementsArray(int[] elementsArray, int startIndex, int numElements) {

        checkClearElementsArrayParameters(elementsArray, elementsArray.length, startIndex, numElements);

        Arrays.fill(elementsArray, startIndex, startIndex + numElements, clearValue);
    }
}
