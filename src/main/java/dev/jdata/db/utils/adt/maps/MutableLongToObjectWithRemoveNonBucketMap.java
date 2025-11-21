package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;

abstract class MutableLongToObjectWithRemoveNonBucketMap<T> extends BaseLongToObjectWithRemoveNonBucketMap<T> implements IMutableLongToObjectWithRemoveStaticMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_TO_OBJECT_WITH_REMOVE_NON_BUCKET_MAP;

    MutableLongToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<T[]> createValuesArray) {
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

        clearBaseLongToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final T put(long key, T value, T defaultPreviousValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final T result = putValue(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final T removeAndReturnPrevious(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final T result;

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask());

        if (indexToRemove != NO_INDEX) {

            final T[] values = getValues();

            result = values[indexToRemove];

            values[indexToRemove] = null;

            decrementNumElements();
        }
        else {
            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(b -> b.add("key", key));
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
