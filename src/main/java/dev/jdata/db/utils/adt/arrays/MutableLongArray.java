package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;

abstract class MutableLongArray extends BaseLongArray implements IMutableLongArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_ARRAY;

    private final long clearValue;

    MutableLongArray(AllocationType allocationType, int initialCapacity) {
        this(allocationType, initialCapacity, 0L, false);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongArray(AllocationType allocationType, int initialCapacity, long clearValue) {
        this(allocationType, initialCapacity, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableLongArray(AllocationType allocationType, int initialCapacity, long clearValue, boolean hasClearValue) {
        super(allocationType, createArray(initialCapacity, clearValue, hasClearValue), 0, hasClearValue);

        this.clearValue = clearValue;
    }

    private static long[] createArray(int initialCapacity, long clearValue, boolean hasClearValue) {

        final long[] array = new long[initialCapacity];

        if (hasClearValue) {

            Arrays.fill(array, clearValue);
        }

        return array;
    }

    MutableLongArray(AllocationType allocationType, MutableLongArray toCopy) {
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
    public final void add(long value) {

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
    public final void set(long index, long value) {

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
    final long[] reallocate(long[] elementsArray, int newCapacity) {

        checkReallocateParameters(elementsArray, elementsArray.length, newCapacity);

        return Arrays.copyOf(elementsArray, newCapacity);
    }

    @Override
    final void clearElementsArray(long[] elementsArray, int startIndex, int numElements) {

        checkClearElementsArrayParameters(elementsArray, elementsArray.length, startIndex, numElements);

        Arrays.fill(elementsArray, startIndex, numElements, clearValue);
    }
}
