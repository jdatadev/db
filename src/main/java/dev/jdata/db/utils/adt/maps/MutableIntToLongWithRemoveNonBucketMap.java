package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

public final class MutableIntToLongWithRemoveNonBucketMap extends BaseIntToLongWithRemoveNonBucketMap implements IMutableIntToLongStaticMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_LONG_WITH_REMOVE_NON_BUCKET_MAP;

    public MutableIntToLongWithRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableIntToLongWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    public MutableIntToLongWithRemoveNonBucketMap(MutableIntToLongWithRemoveNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public final long put(int key, long value, long defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final long result = putValue(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public long removeAndReturnPrevious(int key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long result;

        final int index = removeAndReturnIndex(key);

        if (index != NO_INDEX) {

            result = getValues()[index];
        }
        else {
            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public void remove(int key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = removeAndReturnIndex(key);

        if (index == NO_INDEX) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseIntToLongNonContainsNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
