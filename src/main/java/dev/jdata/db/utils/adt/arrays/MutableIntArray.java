package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.debug.PrintDebug;

public final class MutableIntArray extends BaseIntArray implements IMutableIntArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_ARRAY;

    private static final Class<?> debugClass = MutableIntArray.class;

    public static MutableIntArray copyOf(MutableIntArray toCopy) {

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("toCopy", toCopy));
        }

        final MutableIntArray result = new MutableIntArray(toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private final int clearValue;

    public MutableIntArray(int initialCapacity) {
        this(initialCapacity, 0, false);

        if (DEBUG) {

            enter(b -> b.add("initialCapacity", initialCapacity));
        }

        if (DEBUG) {

            exit();
        }
    }

    public MutableIntArray(int initialCapacity, int clearValue) {
        this(initialCapacity, clearValue, true);

        if (DEBUG) {

            enter(b -> b.add("initialCapacity", initialCapacity).add("clearValue", clearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableIntArray(int initialCapacity, int clearValue, boolean hasClearValue) {
        super(createArray(initialCapacity, clearValue, hasClearValue), 0, hasClearValue);

        this.clearValue = clearValue;
    }

    private static int[] createArray(int initialCapacity, int clearValue, boolean hasClearValue) {

        final int[] array = new int[initialCapacity];

        if (hasClearValue) {

            Arrays.fill(array, clearValue);
        }

        return array;
    }

    private MutableIntArray(MutableIntArray toCopy) {
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
    public void add(int value) {

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
    public void set(long index, int value) {

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
    int[] reallocate(int[] elements, int newCapacity) {

        final int[] array = Arrays.copyOf(elements, newCapacity);

        if (hasClearValue()) {

            Arrays.fill(array, elements.length, array.length, clearValue);
        }

        return array;
    }
}
