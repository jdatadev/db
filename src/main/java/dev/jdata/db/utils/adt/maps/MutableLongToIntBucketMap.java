package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;

abstract class MutableLongToIntBucketMap extends BaseLongToIntBucketMap<MutableLongToIntBucketMap> implements IMutableLongToIntDynamicMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_TO_INT_BUCKET_MAP;

    MutableLongToIntBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongToIntBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long getCapacity() {

        return getHashedCapacity();
    }

    @Override
    public final void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseLongToIntBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final int put(long key, int value, int defaultPreviousValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final int result;

        final long putResult = putValueAndReturnNode(key);
        final long node = getPutResultNode(putResult);

        final LongToIntBucketMapMultiHeadSinglyLinkedList<?> buckets = getBuckets();

        if (isNewAdded(putResult)) {

            result = defaultPreviousValue;

            buckets.setIntValue(node, value);
        }
        else {
            result = buckets.getAndSetIntValue(node, value);
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final int removeAndReturnPrevious(long key, int defaultValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final long removedNode = removeElementAndReturnValueNode(key);

        final int result = removedNode != NO_LONG_NODE ? getBuckets().getIntValue(removedNode) : defaultValue;

        if (DEBUG) {

            exit(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    public final boolean remove(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long node = removeElementAndReturnValueNode(key);

        final boolean result = node != NO_LONG_NODE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }
}
