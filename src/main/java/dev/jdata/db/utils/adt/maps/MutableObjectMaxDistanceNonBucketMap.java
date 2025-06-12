package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;

public final class MutableObjectMaxDistanceNonBucketMap<K, V> extends BaseObjectMaxDistanceNonBucketMap<K, V> implements IMutableObjectDynamicMap<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    public MutableObjectMaxDistanceNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, createKeysArray, createValuesArray);
    }

    public MutableObjectMaxDistanceNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);
    }

    @Override
    public V put(K key, V value, V defaultPreviousValue) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final V result = putMaxDistance(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public V removeAndReturnPrevious(K key, V defaultValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int index = removeAndReturnIndexIfExists(key);

        final V result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    public boolean remove(K key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, getKeyMask());

        final boolean result = removeAndReturnIndexIfExists(key, hashArrayIndex) != NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseObjectToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
