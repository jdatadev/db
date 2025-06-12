package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;

public final class MutableIntToObjectMaxDistanceNonBucketMap<T> extends BaseIntToObjectMaxDistanceNonBucketMap<T> implements IMutableIntToObjectDynamicMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    public MutableIntToObjectMaxDistanceNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, createValuesArray);
    }

    public MutableIntToObjectMaxDistanceNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    @Override
    public T put(int key, T value, T defaultPreviousValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final T result = putMaxDistance(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public T removeAndReturnPrevious(int key, T defaultValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int index = removeAndReturnIndex(key);

        final T result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    public boolean remove(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final boolean result = removeAndReturnIndex(key, hashArrayIndex) != NO_INDEX;

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

        clearBaseIntToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
