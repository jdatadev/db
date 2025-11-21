package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;

abstract class MutableLongToObjectBucketMap<T> extends BaseLongToObjectADTBucketMap<T, MutableLongToObjectBucketMap<T>> implements IMutableLongToObjectDynamicMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_TO_OBJECT_BUCKET_MAP;

    MutableLongToObjectBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<T[][]> createOuterValuesArray, IntFunction<T[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createOuterValuesArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createOuterValuesArray", createOuterValuesArray)
                    .add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongToObjectBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<T[][]> createOuterValuesArray, IntFunction<T[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createOuterValuesArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createOuterValuesArray", createOuterValuesArray).add("createValuesArray", createValuesArray));
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

        clearBaseLongToObjectBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final T put(long key, T value, T defaultPreviousValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final T result;

        final long putResult = putValueAndReturnNode(key);
        final long node = getPutResultNode(putResult);

        final LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<?, T> buckets = getBuckets();

        if (isNewAdded(putResult)) {

            result = defaultPreviousValue;

            buckets.setObjectValue(node, value);
        }
        else {
            result = buckets.getAndSetObjectValue(node, value);
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final T removeAndReturnPrevious(long key, T defaultValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final long removedNode = removeElementAndReturnValueNode(key);

        final T result = removedNode != NO_LONG_NODE ? getBuckets().getObjectValue(removedNode) : defaultValue;

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
