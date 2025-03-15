package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;

public final class ObjectMap<K, V> extends BaseObjectMap<K, V[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_OBJECT_MAP;

    private static final Object NO_VALUE = null;

    public ObjectMap(int initialCapacityExponent, IntFunction<K[]> createKeyArray, IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR, createKeyArray, createValuesArray);
    }

    public ObjectMap(int initialCapacityExponent, float loadFactor, IntFunction<K[]> createKeyArray, IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, loadFactor, createKeyArray, createValuesArray);
    }

    public ObjectMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeyArray, IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeyArray, createValuesArray);
    }

    public V get(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

        @SuppressWarnings("unchecked")
        final V noValue = (V)NO_VALUE;

        final V result = index != NO_INDEX ? getValues()[index] : noValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @FunctionalInterface
    public interface ForEachKeyAndValue<K, V, T> {

        void each(K key, V value, T parameter);
    }

    public <T> void forEachKeyAndValue(T parameter, ForEachKeyAndValue<K, V, T> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        forEachKeyAndValue(parameter, forEachKeyAndValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));
    }

    public void keysAndValues(K[] keysDst, V[] valuesDst) {

        final long numElements = getNumElements();

        Checks.areEqual(keysDst.length, numElements);
        Checks.areEqual(valuesDst.length, numElements);

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src[srcIndex]);
    }

    public void put(K key, V value) {

        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value));
        }

        final int putResult = put(key);

        final int index = getPutIndex(putResult);

        if (index != NO_INDEX) {

            getValues()[index] = value;
        }

        if (DEBUG) {

            exit(b -> b.add("key", key).add("value", value));
        }
    }

    @Override
    protected void put(V[] values, int index, V[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected void clearValues(V[] values) {

        Arrays.fill(values, null);
    }
}
