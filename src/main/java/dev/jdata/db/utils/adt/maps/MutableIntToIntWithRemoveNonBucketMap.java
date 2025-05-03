package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

public final class MutableIntToIntWithRemoveNonBucketMap extends BaseIntToIntWithRemoveNonBucketMap implements IMutableIntToIntWithRemoveNonBucketMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_INT_WITH_REMOVE_NON_BUCKET_MAP;

    public MutableIntToIntWithRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableIntToIntWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    public MutableIntToIntWithRemoveNonBucketMap(BaseIntToIntNonRemoveNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public int put(int key, int value, int defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final int result = putValue(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public void remove(int key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        removeAndReturnIndex(key);

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }
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
