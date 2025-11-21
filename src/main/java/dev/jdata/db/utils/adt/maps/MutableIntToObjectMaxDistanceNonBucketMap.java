package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;

abstract class MutableIntToObjectMaxDistanceNonBucketMap<V> extends BaseIntToObjectMaxDistanceNonBucketMap<V> implements IMutableIntToObjectDynamicMap<V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    MutableIntToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createValuesArray", createValuesArray));
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

        clearBaseIntToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final V put(int key, V value, V defaultPreviousValue) {

        IntNonBucket.checkIsHashArrayElement(key);

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
    public final V removeAndReturnPrevious(int key, V defaultValue) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int indexToRemove = removeMaxDistance(key);

        final V result;

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
    public final boolean remove(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

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
