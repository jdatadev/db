package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseIntToIntNonContainsKeyNonBucketMap extends BaseIntToIntNonBucketMap implements IIntToIntNonContainsNonBucketMapGetters {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_INT_NON_CONTAINS_NON_BUCKET_MAP;

    BaseIntToIntNonContainsKeyNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseIntToIntNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseIntToIntNonContainsKeyNonBucketMap(BaseIntToIntNonRemoveNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public final int get(int key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result;

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

    final int putValue(int key, int value, int defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final int result;

        final long putResult = put(key);
        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final int[] values = getValues();

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
