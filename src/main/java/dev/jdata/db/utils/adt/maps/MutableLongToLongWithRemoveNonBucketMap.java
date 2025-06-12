package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.checks.Checks;

public final class MutableLongToLongWithRemoveNonBucketMap extends BaseLongToLongWithRemoveNonBucketMap implements IMutableLongToLongStaticMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_TO_LONG_NON_BUCKET_MAP;

    public MutableLongToLongWithRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableLongToLongWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    @Override
    public long put(long key, long value, long defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);
        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value));
        }

        final long result;

        final long putResult = put(key);

        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final long[] values = getValues();

            result = values[index];

            values[index] = value;
        }
        else {
            result = defaultPreviousValue;
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value));
        }

        return result;
    }

    @Override
    public void remove(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long result;

        final int index = removeAndReturnIndex(key);

        if (index != NO_INDEX) {

            result = getHashed()[index];
        }
        else {
            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseLongToLongNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
