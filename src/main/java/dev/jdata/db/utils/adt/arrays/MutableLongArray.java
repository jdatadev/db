package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.debug.PrintDebug;

public final class MutableLongArray extends BaseLongArray implements IMutableLongArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_ARRAY;

    private static final Class<?> debugClass = MutableLongArray.class;

    public static MutableLongArray copyOf(MutableLongArray toCopy) {

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("toCopy", toCopy));
        }

        final MutableLongArray result = new MutableLongArray(toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private final long clearValue;

    public MutableLongArray(int initialCapacity) {
        this(initialCapacity, 0L, false);

        if (DEBUG) {

            enter(b -> b.add("initialCapacity", initialCapacity));
        }

        if (DEBUG) {

            exit();
        }
    }

    public MutableLongArray(int initialCapacity, long clearValue) {
        this(initialCapacity, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("initialCapacity", initialCapacity).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableLongArray(int initialCapacity, long clearValue, boolean hasClearValue) {
        super(createArray(initialCapacity, clearValue, hasClearValue), 0, hasClearValue);

        this.clearValue = clearValue;
    }

    private static long[] createArray(int initialCapacity, long clearValue, boolean hasClearValue) {

        final long[] array = new long[initialCapacity];

        if (hasClearValue) {

            Arrays.fill(array, clearValue);
        }

        return array;
    }

    private MutableLongArray(MutableLongArray toCopy) {
        super(toCopy);

        this.clearValue = toCopy.clearValue;
    }

    @Override
    public long getCapacity() {

        return elements.length;
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearArray();

        if (hasClearValue()) {

            Arrays.fill(elements, clearValue);
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void add(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int index = ensureAddIndex();

        elements[index] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void set(long index, long value) {

        if (DEBUG) {

            enter(b -> b.add("index", index).add("value", value));
        }

        final int intIndex = ensureIndex(index);

        elements[intIndex] = value;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    long[] reallocate(long[] elements, int newCapacity) {

        final long[] array = Arrays.copyOf(elements, newCapacity);

        if (hasClearValue()) {

            Arrays.fill(array, elements.length, array.length, clearValue);
        }

        return array;
    }
}
