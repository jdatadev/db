package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;

abstract class MutableLongToLongWithRemoveNonBucketMap extends BaseLongToLongWithRemoveNonBucketMap implements IMutableLongToLongWithRemoveStaticMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_TO_LONG_NON_BUCKET_MAP;

    MutableLongToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongToLongWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
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

        clearBaseLongToLongNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long put(long key, long value, long defaultPreviousValue) {

        LongNonBucket.checkIsHashArrayElement(key);

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
    public final long removeAndReturnPrevious(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long result;

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask());

        if (indexToRemove != NO_INDEX) {

            decrementNumElements();

            result = getValues()[indexToRemove];
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
    public final void remove(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask());

        if (indexToRemove == NO_INDEX) {

            throw new IllegalStateException();
        }

        decrementNumElements();

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }
    }
}
