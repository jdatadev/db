package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class MutableObjectLargeArray<T> extends LimitLargeArray<T[][], T[]> implements IMutableObjectLargeArray<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABlE_OBJECT_LARGE_ARRAY;

    private final IntFunction<T[]> createInnerArray;
    private final T clearValue;

    MutableObjectLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray) {
        this(allocationType, initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray, null, false);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("createOuterArray", createOuterArray).add("createInnerArray", createInnerArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableObjectLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray, T clearValue) {
        this(allocationType, initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("createOuterArray", createOuterArray).add("createInnerArray", createInnerArray).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableObjectLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray, T clearValue, boolean hasClearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, hasClearValue, createOuterArray);

        this.createInnerArray = Objects.requireNonNull(createInnerArray);
        this.clearValue = clearValue;
    }

    MutableObjectLargeArray(AllocationType allocationType, MutableObjectLargeArray<T> toCopy) {
        super(allocationType, toCopy, (s, d, n) -> {

            final int length = s.length;

            Checks.areEqual(length, d.length);

            for (int i = 0; i < n; ++ i) {

                final T[] a = s[i];

                d[i] = a != null ? Array.copyOf(a) : null;
            }
        });

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        this.createInnerArray = toCopy.createInnerArray;
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
    public final T get(long index) {

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public final void add(T value) {

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
    public final void set(long index, T value) {

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
    protected final int getOuterArrayLength(T[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected final T[][] copyOuterArray(T[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThanOrEqualTo(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected final T[] getInnerArray(T[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected final void clearInnerArray(T[] innerArray, long startIndex, long numElements) {

        Objects.requireNonNull(innerArray);
        Checks.checkFromIndexSize(startIndex, numElements, innerArray.length);

        assertShouldClear();

        Arrays.fill(innerArray, IByIndexView.intIndex(startIndex), IByIndexView.intIndex(startIndex + numElements), clearValue);
    }

    @Override
    protected final T[] abstractCreateAndSetInnerArray(T[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isIntInnerElementCapacity(innerArrayElementCapacity);

        final int innerArrayLength = Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);

        return outerArray[outerIndex] = createInnerArray.apply(innerArrayLength);
    }
}
