package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class LargeObjectArray<T> extends LargeLimitArray<T[][], T[]> implements IMutableObjectArray<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_OBJECT_ARRAY;

    private static final Class<?> debugClass = LargeObjectArray.class;

    public static <T> LargeObjectArray<T> copyOf(LargeObjectArray<T> toCopy) {

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("toCopy", toCopy));
        }

        final LargeObjectArray<T> result = new LargeObjectArray<>(toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private final IntFunction<T[]> createInnerArray;
    private final T clearValue;

    public LargeObjectArray(int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createInnerArray) {
        this(initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray, null, false);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("createOuterArray", createOuterArray)
                    .add("createInnerArray", createInnerArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    public LargeObjectArray(int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createInnerArray, T clearValue) {
        this(initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("createOuterArray", createOuterArray)
                    .add("createInnerArray", createInnerArray).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private LargeObjectArray(int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createInnerArray, T clearValue,
            boolean hasClearValue) {
        super(initialOuterCapacity, innerCapacityExponent, hasClearValue, createOuterArray);

        this.createInnerArray = Objects.requireNonNull(createInnerArray);
        this.clearValue = clearValue;
    }

    private LargeObjectArray(LargeObjectArray<T> toCopy) {
        super(toCopy, (s, d, n) -> {

            final int length = s.length;

            Checks.areEqual(length, d.length);

            for (int i = 0; i < n; ++ i) {

                final T[] a = s[i];

                d[i] = a != null ? Array.copyOf(a) : null;
            }
        });

        this.createInnerArray = toCopy.createInnerArray;
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
    public T get(long index) {

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public void add(T value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final long limit = getLimit();

        final T[] array = checkCapacityForOneAppendedElementAndReturnInnerArray(limit);

        final long index = limit;

        incrementLimit();

        array[getInnerElementIndex(index)] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void set(long index, T value) {

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
    protected int getOuterArrayLength(T[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected T[][] copyOuterArray(T[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThanOrEqualTo(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected T[] getInnerArray(T[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected void clearInnerArray(T[] innerArray, long startIndex, long numElements) {

        Objects.requireNonNull(innerArray);
        Checks.checkFromIndexSize(startIndex, numElements, innerArray.length);

        assertShouldClear();

        Arrays.fill(innerArray, Integers.checkUnsignedLongToUnsignedInt(startIndex), Integers.checkUnsignedLongToUnsignedInt(startIndex + numElements), clearValue);
    }

    @Override
    protected T[] abstractCreateAndSetInnerArray(T[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isCapacity(innerArrayElementCapacity);

        final int innerArrayLength = Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);

        return outerArray[outerIndex] = createInnerArray.apply(innerArrayLength);
    }
}
