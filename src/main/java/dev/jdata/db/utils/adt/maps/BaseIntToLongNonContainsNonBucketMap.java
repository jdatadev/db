package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseIntToLongNonContainsNonBucketMap extends BaseIntToLongNonBucketMap implements IIntToLongNonContainsNonBucketMapGetters {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_LONG_NON_CONTAINS_NON_BUCKET_MAP;

    BaseIntToLongNonContainsNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseIntToLongNonContainsNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseIntToLongNonContainsNonBucketMap(BaseIntToLongNonContainsNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public final long get(int key) {

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

    final long putValue(int key, long value, long defaultPreviousValue) {

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

    final void clearBaseIntToLongNonContainsNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
