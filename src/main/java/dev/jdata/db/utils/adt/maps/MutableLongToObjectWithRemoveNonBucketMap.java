package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

public final class MutableLongToObjectWithRemoveNonBucketMap<T> extends BaseLongToObjectWithRemoveNonBucketMap<T> implements IMutableLongToObjectWithRemoveNonBucketMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_TO_OBJECT_WITH_REMOVE_NON_BUCKET_MAP;

    public MutableLongToObjectWithRemoveNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, createArray);
    }

    public MutableLongToObjectWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);
    }

    @Override
    public T put(long key, T value, T defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final T result;

        final long putResult = put(key);

        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final T[] values = getValues();

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

        clearBaseLongToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
