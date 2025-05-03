package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.checks.Checks;

public final class MutableLongToIntWithRemoveNonBucketMap extends BaseLongToIntWithRemoveNonBucketMap implements IMutableLongToIntWithRemoveNonBucketMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_TO_INT_WITH_REMOVE_NON_BUCKET_MAP;

    public MutableLongToIntWithRemoveNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableLongToIntWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    public MutableLongToIntWithRemoveNonBucketMap(MutableLongToIntWithRemoveNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public int put(long key, int value, int defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);
        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final int result;

        final long putResult = put(key);

        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final int[] values = getValues();

            result = values[index];

            values[index] = value;
        }
        else {
            result = defaultPreviousValue;
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public void remove(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = removeAndReturnIndex(key);

        final long result;

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

        clearBaseLongToIntNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
