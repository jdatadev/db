package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;

public final class LongToObjectMap<T> extends BaseLongArrayMap<T[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_TO_OBJECT_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_LONG_TO_OBJECT_MAP;

    public LongToObjectMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createArray);
    }

    public LongToObjectMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createMap", createArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    public T get(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

        final T result = index != NO_INDEX ? getHashed()[index] : null;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    public void put(long key, T value) {

        Checks.isNotNegative(key);
        Objects.requireNonNull(value);

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
    void put(T[] map, int index, T[] newMap, int newIndex) {

        newMap[newIndex] = map[index];
    }
}
