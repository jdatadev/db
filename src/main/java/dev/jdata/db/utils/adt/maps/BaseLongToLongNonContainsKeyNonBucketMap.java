package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseLongToLongNonContainsKeyNonBucketMap extends BaseLongToLongNonBucketMap implements ILongToLongNonContainsNonBucketMapGetters {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_LONG_NON_CONTAINS_NON_BUCKET_MAP;

    BaseLongToLongNonContainsKeyNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseLongToLongNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToLongNonContainsKeyNonBucketMap(BaseLongToLongNonContainsKeyNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public final long get(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long result;

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

    final long putValue(long key, long value, long defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final long result;

        final long putResult = put(key);
        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final long[] values = getValues();

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
