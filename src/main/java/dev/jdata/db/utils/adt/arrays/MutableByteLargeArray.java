package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Bytes;
import dev.jdata.db.utils.scalars.Integers;

abstract class MutableByteLargeArray extends BaseByteLargeArray implements IMutableByteLargeArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABlE_BYTE_LARGE_ARRAY;

    private static final byte NO_CLEAR_VALUE = 0;

    private static final boolean REQUIRES_INNER_ARRAY_NUM_ELEMENTS = false;

    private long limit;

    MutableByteLargeArray(AllocationType allocationType, int innerCapacityExponent) {
        this(allocationType,  DEFAULT_INITIAL_OUTER_CAPACITY, innerCapacityExponent, NO_CLEAR_VALUE, false);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableByteLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        this(allocationType, initialOuterCapacity, innerCapacityExponent, NO_CLEAR_VALUE, false);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableByteLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {
        this(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableByteLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, byte clearValue, boolean hasClearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue, hasClearValue, REQUIRES_INNER_ARRAY_NUM_ELEMENTS);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("clearValue", clearValue).add("hasClearValue", hasClearValue));
        }

        this.limit = 0L;

        if (DEBUG) {

            exit();
        }
    }

    MutableByteLargeArray(AllocationType allocationType, MutableByteLargeArray toCopy) {
        super(allocationType, toCopy);
    }

    @Override
    public final long getCapacity() {

        return getArrayElementCapacity();
    }

    @Override
    public final void clear() {

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
    public final void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public final void toHexString(long index, StringBuilder sb) {

        Bytes.toHexUnsigned(get(index), sb);
    }

    @Override
    public final long getLimit() {

        return limit;
    }

    @Override
    public final byte get(long index) {

        return getByteArrayByOffset(index)[getInnerElementIndex(index)];
    }

    @Override
    public final void add(byte value) {

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

    @Override
    public final void set(long index, byte value) {

        if (DEBUG) {

            enter(b -> b.add("index", index).add("value", value));
        }

        if (index >= limit) {

            ensureCapacityAndLimitAndReturnOuterIndex(index, limit, this, (t, i) -> t.limit += i, shouldClear());
        }
        else {
            Checks.checkLongIndex(index, getNumElements());
        }

        final byte[] array = getByteArrayByOffset(index);

        array[getInnerElementIndex(index)] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final int getInnerByteArrayLength(long innerArrayElementCapacity) {

        return Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);
    }

    private long getNumElements() {

        return limit;
    }

    private void incrementNumElements() {

        ++ limit;
    }
}
