package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;

public final class MutableIntToIntMaxDistanceNonBucketMap extends BaseIntToIntMaxDistanceNonBucketMap implements IMutableIntToIntDynamicMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_INT_MAX_DISTANCE_NON_BUCKET_MAP;

    public MutableIntToIntMaxDistanceNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableIntToIntMaxDistanceNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    public MutableIntToIntMaxDistanceNonBucketMap(BaseIntToIntNonRemoveNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public int put(int key, int value, int defaultPreviousValue) {

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
    public int removeAndReturnPrevious(int key, int defaultValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int index = removeAndReturnIndex(key);

        final int result = index != NO_INDEX ? getValues()[index] : defaultValue;

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

        final int index = removeAndReturnIndex(key, hashArrayIndex);

        final boolean result = index != NO_INDEX;

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

        clearBaseIntToIntNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
