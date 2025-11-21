package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

abstract class MutableObjectMaxDistanceNonBucketMap<K, V> extends BaseObjectToObjectMaxDistanceNonBucketMap<K, V> implements IMutableDynamicMap<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_OBJECT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    MutableObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createKeysArray", createKeysArray)
                    .add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long getCapacity() {

        return getHashedCapacity();
    }

    @Override
    public final void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseObjectToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final V put(K key, V value, V defaultPreviousValue) {

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
    public final V removeAndReturnPrevious(K key, V defaultValue) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final V result;

        final int indexToRemove = removeMaxDistance(key);

        if (indexToRemove != NO_INDEX) {

            final V[] values = getValues();

            result = values[indexToRemove];

            values[indexToRemove] = null;
        }
        else {
            result = defaultValue;
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    public final boolean remove(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final boolean result;

        final int indexToRemove = removeMaxDistance(key);

        if (indexToRemove != NO_INDEX) {

            getValues()[indexToRemove] = null;

            result = true;
        }
        else {
            result = false;
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }
}
