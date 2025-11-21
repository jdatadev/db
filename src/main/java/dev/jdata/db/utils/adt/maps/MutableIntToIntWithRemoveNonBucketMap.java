package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;

abstract class MutableIntToIntWithRemoveNonBucketMap extends BaseIntToIntWithRemoveNonBucketMap implements IMutableIntToIntWithRemoveStaticMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_INT_WITH_REMOVE_NON_BUCKET_MAP;

    MutableIntToIntWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntToIntWithRemoveNonBucketMap(AllocationType allocationType, BaseIntToIntNonRemoveNonBucketMap toCopy) {
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

        clearBaseNonBucketMap();

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

        final int result = putValue(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final int removeAndReturnPrevious(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result;

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask());

        if (indexToRemove != NO_INDEX) {

            result = getValues()[indexToRemove];

            decrementNumElements();
        }
        else {
            throw MapExceptions.noSuchKeyException();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final void remove(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask());

        if (indexToRemove == NO_INDEX) {

            throw MapExceptions.noSuchKeyException();
        }

        decrementNumElements();

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }
    }
}
