package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;

public final class MutableObjectWithRemoveNonBucketMap<K, V> extends BaseObjectWithRemoveNonBucketMap<K, V> implements IMutableObjectWithRemoveNonBucketMap<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_OBJECT_WITH_REMOVE_NON_BUCKET_MAP;

    public MutableObjectWithRemoveNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeyArray, IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createKeyArray, createValuesArray);
    }

    public MutableObjectWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeyArray,
            IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeyArray, createValuesArray);
    }

    @Override
    public V put(K key, V value, V defaultPreviousValue) {

        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final V result;

        final int putResult = put(key);

        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final V[] values = getValues();

            result = values[index];

            values[index] = value;
        }
        else {
            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public void remove(K key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        if (removeAndReturnIndex(key) != NO_INDEX) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }
    }

    @Override
    public void clear() {

        clearBaseObjectNonBucketMap();
    }
}
