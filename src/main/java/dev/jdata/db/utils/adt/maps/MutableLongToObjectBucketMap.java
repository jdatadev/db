package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.lists.BaseList;

public final class MutableLongToObjectBucketMap<T> extends BaseLongToObjectBucketMap<T, MutableLongToObjectBucketMap<T>> implements IMutableLongToObjectDynamicMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_TO_INT_BUCKET_MAP;

    public MutableLongToObjectBucketMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, createArray);
    }

    public MutableLongToObjectBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);
    }

    @Override
    public T put(long key, T value, T defaultPreviousValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final T result;

        final long putResult = putValueAndReturnNode(key);

        if (isNewAdded(putResult)) {

            result = getBuckets().getObjectValue(getPutResultNode(putResult));
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
    public T removeAndReturnPrevious(long key, T defaultValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final long removedNode = removeElementAndReturnValueNode(key);

        final T result = removedNode != BaseList.NO_NODE ? getBuckets().getObjectValue(removedNode) : defaultValue;

        if (DEBUG) {

            exit(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    public boolean remove(long key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long node = removeElementAndReturnValueNode(key);

        final boolean result = node != BaseList.NO_NODE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseLongToObjectBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
