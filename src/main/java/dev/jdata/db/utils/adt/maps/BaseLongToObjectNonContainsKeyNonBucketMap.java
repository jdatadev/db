package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseLongToObjectNonContainsKeyNonBucketMap<T> extends BaseLongToObjectNonBucketMap<T> implements ILongToObjectNonContainsNonBucketMapGetters<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_NON_CONTAINS_NON_BUCKET_MAP;

    BaseLongToObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, createArray);
    }

    BaseLongToObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);
    }

    @Override
    public final T get(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final T result;

        final int index = getIndexScanEntireHashArray(key);

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

    final T putValue(int key, T value, T defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final T result;

        final long putResult = put(key);
        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final T[] values = getValues();

            result = IntPutResult.getPutNewAdded(putResult) ? values[index] : defaultPreviousValue;

            values[index] = value;
        }
        else {
            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }
}
