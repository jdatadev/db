package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.debug.PrintDebug;

public final class LongArray extends BaseLongArray implements ILongArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_ARRAY;

    private static final Class<?> debugClass = LongArray.class;

    public static LongArray of(long ... values) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("values", values));
        }

        final LongArray result = new LongArray(values);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    public static LongArray copyOf(LongArray toCopy) {

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("toCopy", toCopy));
        }

        final LongArray result = new LongArray(toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private LongArray(long[] elements) {
        super(elements, elements.length, false);
    }

    private LongArray(LongArray toCopy) {
        super(toCopy);
    }

    @Override
    long[] reallocate(long[] elements, int newCapacity) {

        throw new UnsupportedOperationException();
    }
}
