package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class MutableIntLargeArray extends LimitLargeArray<int[][], int[]> implements IMutableIntLargeArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABlE_INT_LARGE_ARRAY;

    private final int clearValue;

    MutableIntLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        this(allocationType, initialOuterCapacity, innerCapacityExponent, 0, false);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, int clearValue) {
        this(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, int clearValue, boolean hasClearValue) {
        super(allocationType,  initialOuterCapacity, innerCapacityExponent, hasClearValue, int[][]::new);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("clearValue", clearValue).add("hasClearValue", hasClearValue));
        }

        this.clearValue = clearValue;

        if (DEBUG) {

            exit();
        }
    }

    MutableIntLargeArray(AllocationType allocationType, MutableIntLargeArray toCopy) {
        super(allocationType, toCopy, (s, d, n) -> {

            final int length = s.length;

            Checks.areEqual(length, d.length);

            for (int i = 0; i < n; ++ i) {

                final int[] a = s[i];

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
    public final int get(long index) {

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public final void add(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final long limit = getLimit();

        final int[] innerArray = checkCapacityForOneAppendedElementAndReturnInnerArray(limit);

        incrementLimit();

        final long index = limit;

        innerArray[getInnerElementIndex(index)] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final void set(long index, int value) {

        Checks.isLongIndexNotOutOfBounds(index);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("value", value));
        }

        final int[] innerArray = ensureCapacityAndLimitAndReturnInnerArray(index, shouldClear());

        innerArray[getInnerElementIndex(index)] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final int getOuterArrayLength(int[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected final int[][] copyOuterArray(int[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThanOrEqualTo(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected final int[] getInnerArray(int[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected final void clearInnerArray(int[] innerArray, long startIndex, long numElements) {

        Objects.requireNonNull(innerArray);
        Checks.checkFromIndexSize(startIndex, numElements, innerArray.length);

        assertShouldClear();

        Arrays.fill(innerArray, IByIndexView.intIndex(startIndex), IByIndexView.intIndex(startIndex + numElements), clearValue);
    }

    @Override
    protected final int[] abstractCreateAndSetInnerArray(int[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isIntInnerElementCapacity(innerArrayElementCapacity);

        final int innerArrayLength = Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);

        return outerArray[outerIndex] = new int[innerArrayLength];
    }
}
