package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;

abstract class MutableIntToIntMaxDistanceNonBucketMap extends BaseIntToIntMaxDistanceNonBucketMap implements IMutableIntToIntDynamicMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_INT_MAX_DISTANCE_NON_BUCKET_MAP;

    MutableIntToIntMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntToIntMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntToIntMaxDistanceNonBucketMap(AllocationType allocationType, MutableIntToIntMaxDistanceNonBucketMap toCopy) {
        super(allocationType, toCopy);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
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

        clearBaseIntToIntNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final int put(int key, int value, int defaultPreviousValue) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final int result = putMaxDistance(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final int removeAndReturnPrevious(int key, int defaultValue) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int indexToRemove = removeMaxDistance(key);

        final int result = indexToRemove != NO_INDEX ? getValues()[indexToRemove] : defaultValue;

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

        final int indexToRemove = removeMaxDistance(key);

        final boolean result = indexToRemove != NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }
}
