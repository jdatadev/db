package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class LargeByteArray extends BaseLargeByteArray implements IMutableArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_BYTE_ARRAY;

    private static final byte NO_CLEAR_VALUE = 0;

    private static final boolean REQUIRES_INNER_ARRAY_NUM_ELEMENTS = false;

    private long limit;

    public LargeByteArray(int innerCapacityExponent) {
        this(DEFAULT_INITIAL_OUTER_CAPACITY, innerCapacityExponent, NO_CLEAR_VALUE, false);

        if (DEBUG) {

            enter(b -> b.add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    public LargeByteArray(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, innerCapacityExponent, NO_CLEAR_VALUE, false);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    public LargeByteArray(int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {
        this(initialOuterCapacity, innerCapacityExponent, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private LargeByteArray(int initialOuterCapacity, int innerCapacityExponent, byte clearValue, boolean hasClearValue) {
        super(initialOuterCapacity, innerCapacityExponent, clearValue, hasClearValue, REQUIRES_INNER_ARRAY_NUM_ELEMENTS);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue)
                    .add("hasClearValue", hasClearValue));
        }

        this.limit = 0L;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public long getCapacity() {

        return getArrayElementCapacity();
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearArray();

        this.limit = 0L;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public long getLimit() {

        return limit;
    }

    public int get(long index) {

        return getByteArrayByOffset(index)[getInnerElementIndex(index)];
    }

    public void add(byte value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final byte[] byteArray = checkCapacityForOneAppendedElementAndReturnInnerArray(limit);

        byteArray[getInnerElementIndex(getNumElements())] = value;

        incrementNumElements();

        if (DEBUG) {

            exit();
        }
    }

    public void set(long index, byte value) {

        if (DEBUG) {

            enter(b -> b.add("index", index).add("value", value));
        }

        if (index >= limit) {

            ensureCapacityAndLimitAndReturnOuterIndex(index, limit, this, (t, i) -> t.limit += i, shouldClear());
        }
        else {
            Checks.checkIndex(index, getNumElements());
        }

        final byte[] array = getByteArrayByOffset(index);

        array[getInnerElementIndex(index)] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected int getInnerByteArrayLength(long innerArrayElementCapacity) {

        return Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);
    }

    private long getNumElements() {

        return limit;
    }

    private void incrementNumElements() {

        ++ limit;
    }
}
