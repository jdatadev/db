package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;

public final class IntToLongMap extends BaseIntArrayMap<long[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_INT_TO_LONG_MAP;

    public static final long NO_VALUE = -1L;

    public IntToLongMap(int initialCapacityExponent) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public IntToLongMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    public long get(int key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

        final long result = index != NO_INDEX ? getValues()[index] : NO_VALUE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @FunctionalInterface
    public interface ForEachKeyAndValue<T> {

        void each(int key, long value, T parameter);
    }

    public <T> void forEachKeyAndValue(T parameter, ForEachKeyAndValue<T> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        forEachKeyAndValue(parameter, forEachKeyAndValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));
    }

    public void keysAndValues(int[] keysDst, long[] valuesDst) {

        final long numElements = getNumElements();

        Checks.areEqual(keysDst.length, numElements);
        Checks.areEqual(valuesDst.length, numElements);

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src[srcIndex]);
    }

    public void put(int key, long value) {

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

    public long removeAndReturnValue(int key) {

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
    protected void put(long[] values, int index, long[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected void clearValues(long[] valuesArray) {

    }
}
