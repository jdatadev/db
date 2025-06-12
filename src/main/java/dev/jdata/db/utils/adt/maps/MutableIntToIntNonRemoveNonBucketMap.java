package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

public final class MutableIntToIntNonRemoveNonBucketMap extends BaseIntToIntNonRemoveNonBucketMap implements IMutableIntToIntNonRemoveStaticMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_INT_NON_REMOVE_NON_BUCKET_MAP;

    public MutableIntToIntNonRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableIntToIntNonRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    public MutableIntToIntNonRemoveNonBucketMap(MutableIntToIntNonRemoveNonBucketMap toCopy) {
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
