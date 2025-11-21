package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Bytes;
import dev.jdata.db.utils.scalars.Integers;

final class MutableByteLargeArray extends BaseByteLargeArray implements IMutableByteLargeArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABlE_BYTE_LARGE_ARRAY;

    private static final Class<?> debugClass = MutableByteLargeArray.class;

    static MutableByteLargeArray copyOf(MutableByteLargeArray toCopy) {

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("toCopy", toCopy));
        }

        final MutableByteLargeArray result = new MutableByteLargeArray(toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static final byte NO_CLEAR_VALUE = 0;

    private static final boolean REQUIRES_INNER_ARRAY_NUM_ELEMENTS = false;

    private long limit;

    MutableByteLargeArray(int innerCapacityExponent) {
        this(DEFAULT_INITIAL_OUTER_CAPACITY, innerCapacityExponent, NO_CLEAR_VALUE, false);

        if (DEBUG) {

            enter(b -> b.add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableByteLargeArray(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, innerCapacityExponent, NO_CLEAR_VALUE, false);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableByteLargeArray(int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {
        this(initialOuterCapacity, innerCapacityExponent, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableByteLargeArray(int initialOuterCapacity, int innerCapacityExponent, byte clearValue, boolean hasClearValue) {
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

    MutableByteLargeArray(MutableByteLargeArray toCopy) {
        super(toCopy);
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
    public void toHexString(long index, StringBuilder sb) {

        Bytes.toHexUnsigned(get(index), sb);
    }

    @Override
    public long getLimit() {

        return limit;
    }

    @Override
    public byte get(long index) {

        return getByteArrayByOffset(index)[getInnerElementIndex(index)];
    }

    @Override
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

    @Override
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
