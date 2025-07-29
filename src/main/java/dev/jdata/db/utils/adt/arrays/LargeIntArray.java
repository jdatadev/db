package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class LargeIntArray extends LargeLimitArray<int[][], int[]> implements IMutableIntArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_INT_ARRAY;

    private static final Class<?> debugClass = LargeIntArray.class;

    public static LargeIntArray copyOf(LargeIntArray toCopy) {

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("toCopy", toCopy));
        }

        final LargeIntArray result = new LargeIntArray(toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private final int clearValue;

    public LargeIntArray(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, innerCapacityExponent, 0, false);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    public LargeIntArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {
        this(initialOuterCapacity, innerCapacityExponent, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    LargeIntArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue, boolean hasClearValue) {
        super(initialOuterCapacity, innerCapacityExponent, hasClearValue, int[][]::new);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue)
                    .add("hasClearValue", hasClearValue));
        }

        this.clearValue = clearValue;

        if (DEBUG) {

            exit();
        }
    }

    private LargeIntArray(LargeIntArray toCopy) {
        super(toCopy, (s, d, n) -> {

            final int length = s.length;

            Checks.areEqual(length, d.length);

            for (int i = 0; i < n; ++ i) {

                final int[] a = s[i];

                d[i] = a != null ? Array.copyOf(a) : null;
            }
        });

        this.clearValue = toCopy.clearValue;
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

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public int get(long index) {

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public void add(int value) {

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
    public void set(long index, int value) {

        Checks.isIndexNotOutOfBounds(index);

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
    protected int getOuterArrayLength(int[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected int[][] copyOuterArray(int[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThanOrEqualTo(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected int[] getInnerArray(int[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected void clearInnerArray(int[] innerArray, long startIndex, long numElements) {

        Objects.requireNonNull(innerArray);
        Checks.checkFromIndexSize(startIndex, numElements, innerArray.length);

        assertShouldClear();

        Arrays.fill(innerArray, Integers.checkUnsignedLongToUnsignedInt(startIndex), Integers.checkUnsignedLongToUnsignedInt(startIndex + numElements), clearValue);
    }

    @Override
    protected int[] abstractCreateAndSetInnerArray(int[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isCapacity(innerArrayElementCapacity);

        final int innerArrayLength = Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);

        return outerArray[outerIndex] = new int[innerArrayLength];
    }
}
