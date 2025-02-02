package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class IntToObjectMap<T> extends BaseIntArrayMap<T[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_INT_TO_OBJECT_MAP;

    public IntToObjectMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createArray);
    }

    public IntToObjectMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createMap", createArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    public T get(int key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

        final T result = index != NO_INDEX ? getValues()[index] : null;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @FunctionalInterface
    public interface ForEachKeyAndValue<T, U> {

        void each(int key, T value, U parameter);
    }

    public <I> void forEachKeyAndValue(I parameter, ForEachKeyAndValue<T, I> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        forEachKeyAndValue(parameter, forEachKeyAndValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));
    }

    public void keysAndValues(int[] keysDst, T[] valuesDst) {

        final long numElements = getNumElements();

        Checks.areEqual(keysDst.length, numElements);
        Checks.areEqual(valuesDst.length, numElements);

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src[srcIndex]);
    }

    public void put(int key, T value) {

        Checks.isNotNegative(key);
        Objects.requireNonNull(value);

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

    @Override
    protected void put(T[] values, int index, T[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected void clearValues(T[] values) {

        Arrays.fill(values, null);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 100));

        sb.append(getClass().getSimpleName()).append(" {");

        final int prefixLength = sb.length();

        forEachKeyAndValue(sb, (k, v, b) -> {

            if (b.length() > prefixLength) {

                b.append(',');
            }

            b.append(k).append('=').append(v);
        });

        sb.append('}');

        return sb.toString();
    }
}
