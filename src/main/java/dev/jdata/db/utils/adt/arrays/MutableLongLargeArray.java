package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class MutableLongLargeArray extends LimitLargeArray<long[][], long[]> implements IMutableLongLargeArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABlE_LONG_LARGE_ARRAY;

    private final long clearValue;

    MutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        this(allocationType, initialOuterCapacity, innerCapacityExponent, 0L, false);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, long clearValue) {
        this(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, long clearValue, boolean hasClearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, hasClearValue, long[][]::new);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("clearValue", clearValue).add("hasClearValue", hasClearValue));
        }

        this.clearValue = clearValue;

        if (DEBUG) {

            exit();
        }
    }

    MutableLongLargeArray(AllocationType allocationType, MutableLongLargeArray toCopy) {
        super(allocationType, toCopy, (s, d, n) -> {

            final int length = s.length;

            Checks.areEqual(length, d.length);

            for (int i = 0; i < n; ++ i) {

                final long[] a = s[i];

                d[i] = a != null ? Array.copyOf(a) : null;
            }
        });

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

        return getArrayElementCapacity();
    }

    @Override
    public final void clear() {

        if (DEBUG) {

            enter();
        }

        clearArray();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long get(long index) {

        Checks.checkLongIndex(index, getLimit());

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public final void add(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final long limit = getLimit();

        final long[] array = checkCapacityForOneAppendedElementAndReturnInnerArray(limit);

        final long index = limit;

        incrementLimit();

        array[getInnerElementIndex(index)] = value;

        if (DEBUG) {

            exit(value);
        }
    }

    @Override
    public final void set(long index, long value) {

        Checks.isLongIndexNotOutOfBounds(index);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("value", value));
        }

        final int outerIndex = ensureCapacityAndLimitAndReturnOuterIndex(index, shouldClear());

        getOuterArray()[outerIndex][getInnerElementIndex(index)] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final int getOuterArrayLength(long[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected final long[][] copyOuterArray(long[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThanOrEqualTo(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected final long[] getInnerArray(long[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected final void clearInnerArray(long[] innerArray, long startIndex, long numElements) {

        Objects.requireNonNull(innerArray);
        Checks.checkFromIndexSize(startIndex, numElements, innerArray.length);

        assertShouldClear();

        Arrays.fill(innerArray, IByIndexView.intIndex(startIndex), IByIndexView.intIndex(startIndex + numElements), clearValue);
    }

    @Override
    protected final long[] abstractCreateAndSetInnerArray(long[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isIntInnerElementCapacity(innerArrayElementCapacity);

        final int innerArrayLength = Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);

        return outerArray[outerIndex] = new long[innerArrayLength];
    }
}
