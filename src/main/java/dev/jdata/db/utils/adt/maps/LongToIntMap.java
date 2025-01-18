package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

@Deprecated // currently not in use, also calls delegate
public final class LongToIntMap implements PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_TO_INT_MAP;

    public static final int NO_VALUE = -1;

    private final LongToLongMap delegate;

    public LongToIntMap(int initialCapacityExponent) {

        this.delegate = new LongToLongMap(initialCapacityExponent);
    }

    public long get(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long value = delegate.get(key);

        final int result = value != LongToLongMap.NO_VALUE ? (int)value : NO_VALUE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    public void put(long key, int value) {

        Checks.isNotNegative(key);
        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value));
        }

        delegate.put(key, value);

        if (DEBUG) {

            exit(b -> b.add("key", key).add("value", value));
        }
    }
}
