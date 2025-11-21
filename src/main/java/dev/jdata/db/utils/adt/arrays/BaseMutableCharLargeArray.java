package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableCharLargeArray extends LimitLargeArray<char[][], char[]> implements IMutableCharLargeArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_MUTABLE_CHAR_LARGE_ARRAY;

    private final char clearValue;

    BaseMutableCharLargeArray(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, innerCapacityExponent, '\0', true);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseMutableCharLargeArray(int initialOuterCapacity, int innerCapacityExponent, char clearValue) {
        this(initialOuterCapacity, innerCapacityExponent, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseMutableCharLargeArray(BaseMutableCharLargeArray toCopy) {
        super(toCopy, (s, d, n) -> {

            final int length = s.length;

            Checks.areEqual(length, d.length);

            for (int i = 0; i < n; ++ i) {

                final char[] a = s[i];

                d[i] = a != null ? Array.copyOf(a) : null;
            }
        });

        this.clearValue = toCopy.clearValue;
    }

    private BaseMutableCharLargeArray(int initialOuterCapacity, int innerCapacityExponent, char clearValue, boolean hasClearValue) {
        super(initialOuterCapacity, innerCapacityExponent, hasClearValue, char[][]::new);

        this.clearValue = clearValue;
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
    public final char get(long index) {

        Checks.checkIndex(index, getLimit());

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public final void add(char value) {

        final long limit = getLimit();

        final char[] array = checkCapacityForOneAppendedElementAndReturnInnerArray(limit);

        final long index = limit;

        incrementLimit();

        array[getInnerElementIndex(index)] = value;
    }

    @Override
    public final char setAndReturnPrevious(long index, char value) {

        Checks.isIndex(index);

        final char[][] outerArray = getOuterArray();
        final int outerIndex = ensureCapacityAndLimitAndReturnOuterIndex(index, shouldClear());
        final int innerIndex = getInnerElementIndex(index);

        final char result = outerArray[outerIndex][innerIndex];

        outerArray[outerIndex][innerIndex] = value;

        return result;
    }

    @Override
    protected final char[][] copyOuterArray(char[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThan(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected final int getOuterArrayLength(char[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected final char[] getInnerArray(char[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected final void clearInnerArray(char[] innerArray, long startIndex, long numElements) {

        Objects.requireNonNull(innerArray);
        Checks.checkFromIndexSize(startIndex, numElements, innerArray.length);

        assertShouldClear();

        Arrays.fill(innerArray, IByIndexView.intIndex(startIndex), IByIndexView.intIndex(startIndex + numElements), clearValue);
    }

    @Override
    protected final char[] abstractCreateAndSetInnerArray(char[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isCapacity(innerArrayElementCapacity);

        final int innerArrayLength = Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);

        return outerArray[outerIndex] = new char[innerArrayLength];
    }
}
