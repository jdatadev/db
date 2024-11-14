package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;

public final class LongToLongMap extends BaseLongArrayMap<long[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_TO_LONG_MAP;

    public static final long NO_VALUE = -1L;

    public LongToLongMap(int initialCapacityExponent) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public LongToLongMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    public long get(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

        final long result = index != NO_INDEX ? getHashed()[index] : NO_VALUE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    public void put(long key, long value) {

        Checks.isNotNegative(key);
        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value));
        }

        final long putResult = put(key);

        final int index = getPutIndex(putResult);

        if (index != NO_INDEX) {

            getHashed()[index] = value;
        }

        if (DEBUG) {

            exit(b -> b.add("key", key).add("value", value));
        }
    }

    @Override
    void put(long[] map, int index, long[] newMap, int newIndex) {

        newMap[newIndex] = map[index];
    }
}
