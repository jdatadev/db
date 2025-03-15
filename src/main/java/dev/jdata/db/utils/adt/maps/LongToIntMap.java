package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;

public final class LongToIntMap extends BaseLongArrayMap<int[]> implements LongToIntMapGetters {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_TO_INT_MAP;

    public static final int NO_VALUE = -1;

    public LongToIntMap(int initialCapacityExponent) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public LongToIntMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public int get(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

        final int result = index != NO_INDEX ? getValues()[index] : NO_VALUE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @FunctionalInterface
    public interface ForEachKeyAndValue<T> {

        void each(long key, int value, T parameter);
    }

    public <T> void forEachKeyAndValue(T parameter, ForEachKeyAndValue<T> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        forEachKeyAndValue(parameter, forEachKeyAndValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));
    }

    public void keysAndValues(long[] keysDst, int[] valuesDst) {

        final long numElements = getNumElements();

        Checks.areEqual(keysDst.length, numElements);
        Checks.areEqual(valuesDst.length, numElements);

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src[srcIndex]);
    }

    public void put(long key, int value) {

        Checks.isNotNegative(key);
        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value));
        }

        final long putResult = put(key);

        final int index = getPutIndex(putResult);

        if (index != NO_INDEX) {

            getValues()[index] = value;
        }

        if (DEBUG) {

            exit(b -> b.add("key", key).add("value", value));
        }
    }

    public long removeAndReturnValue(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = removeAndReturnIndex(key);

        final long result = index != NO_INDEX ? getHashed()[index] : NO_VALUE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    protected void put(int[] values, int index, int[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected void clearValues(int[] valuesArray) {

    }
}
