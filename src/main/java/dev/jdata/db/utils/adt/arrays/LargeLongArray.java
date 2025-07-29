package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.ObjLongConsumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.strings.StringBuilders;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class LargeLongArray extends LargeLimitArray<long[][], long[]> implements IMutableLongArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_LONG_ARRAY;

    private static final Class<?> debugClass = LargeLongArray.class;

    public static LargeLongArray copyOf(LargeLongArray toCopy) {

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("toCopy", toCopy));
        }

        final LargeLongArray result = new LargeLongArray(toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private final long clearValue;

    public LargeLongArray(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, innerCapacityExponent, 0L, false);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    public LargeLongArray(int initialOuterCapacity, int innerCapacityExponent, long clearValue) {
        this(initialOuterCapacity, innerCapacityExponent, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    LargeLongArray(int initialOuterCapacity, int innerCapacityExponent, long clearValue, boolean hasClearValue) {
        super(initialOuterCapacity, innerCapacityExponent, hasClearValue, long[][]::new);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue)
                    .add("hasClearValue", hasClearValue));
        }

        this.clearValue = clearValue;

        if (DEBUG) {

            exit();
        }
    }

    private LargeLongArray(LargeLongArray toCopy) {
        super(toCopy, (s, d, n) -> {

            final int length = s.length;

            Checks.areEqual(length, d.length);

            for (int i = 0; i < n; ++ i) {

                final long[] a = s[i];

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
    public long get(long index) {

        Checks.checkIndex(index, getLimit());

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public void add(long value) {

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
    public void set(long index, long value) {

        Checks.isIndexNotOutOfBounds(index);

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
    protected int getOuterArrayLength(long[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected long[][] copyOuterArray(long[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThanOrEqualTo(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected long[] getInnerArray(long[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected void clearInnerArray(long[] innerArray, long startIndex, long numElements) {

        Objects.requireNonNull(innerArray);
        Checks.checkFromIndexSize(startIndex, numElements, innerArray.length);

        assertShouldClear();

        Arrays.fill(innerArray, Integers.checkUnsignedLongToUnsignedInt(startIndex), Integers.checkUnsignedLongToUnsignedInt(startIndex + numElements), clearValue);
    }

    @Override
    protected long[] abstractCreateAndSetInnerArray(long[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isCapacity(innerArrayElementCapacity);

        final int innerArrayLength = Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);

        return outerArray[outerIndex] = new long[innerArrayLength];
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        toString(sb, StringBuilder::append);

        return sb.toString();
    }

    public void toString(StringBuilder sb, ObjLongConsumer<StringBuilder> appender) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(appender);

        ByIndex.toString(this, 0L, getLimit(), sb, null, appender, null, null, (instance, index, b, a) -> a.accept(b, instance.get(index)));
    }

    public String toHexString() {

        final StringBuilder sb = new StringBuilder();

        toHexString(sb);

        return sb.toString();
    }

    public void toHexString(StringBuilder sb) {

        Objects.requireNonNull(sb);

        toString(sb, (b, element) -> StringBuilders.hexString(b, element, true, 16));
    }
}
