package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.lists.BaseList;

public final class MutableLongToIntBucketMap extends BaseLongToIntBucketMap<MutableLongToIntBucketMap> implements IMutableLongToIntMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_TO_INT_BUCKET_MAP;

    public MutableLongToIntBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableLongToIntBucketMap(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor);
    }

    @Override
    public int put(long key, int value, int defaultPreviousValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final long putResult = putValueAndReturnNode(key);

        final int result;

        if (isNewAdded(putResult)) {

            result = getBuckets().getIntValue(getPutResultNode(putResult));
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
    public int removeAndReturnPrevious(long key, int defaultValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final long removedNode = removeElementAndReturnValueNode(key);

        final int result = removedNode != BaseList.NO_NODE ? getBuckets().getIntValue(removedNode) : defaultValue;

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

        clearBaseLongToIntBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
