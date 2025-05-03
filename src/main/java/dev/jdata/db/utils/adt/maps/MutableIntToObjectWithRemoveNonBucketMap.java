package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

public final class MutableIntToObjectWithRemoveNonBucketMap<T> extends BaseIntToObjectWithRemoveNonBucketMap<T> implements IMutableIntToObjectWithRemoveNonBucketMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_OBJECT_NON_BUCKET_MAP;

    public MutableIntToObjectWithRemoveNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, createValuesArray);
    }

    public MutableIntToObjectWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    @Override
    public T put(int key, T value, T defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);
        Objects.requireNonNull(value);

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

            exit(result, b -> b.add("key", key).add("value", value));
        }

        return result;
    }

    @Override
    public T removeAndReturnPrevious(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final T result;

        final int index = removeAndReturnIndex(key);

        if (index != NO_INDEX) {

            result = getValues()[index];
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
    public void remove(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = removeAndReturnIndex(key);

        if (index == NO_INDEX) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseIntToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
