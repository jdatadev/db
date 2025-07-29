package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongToObjectBucketMap<

                T,
                LIST extends BaseLongToObjectBucketMapMultiHeadSinglyLinkedList<MAP, T, LIST, VALUES>,
                VALUES extends BaseLongToObjectValues<MAP, T, LIST, VALUES>,
                MAP extends BaseLongToObjectBucketMap<T, LIST, VALUES, MAP>>

        extends BaseLongKeyBucketMap<LIST, VALUES, MAP>
        implements ILongToObjectCommonMapGetters<T>, ILongToObjectDynamicMapGetters<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_BUCKET_MAP;

    BaseLongToObjectBucketMap(int initialCapacityExponent, BiIntToObjectFunction<LIST> createBuckets) {
        super(initialCapacityExponent, createBuckets);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("createBuckets", createBuckets));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongToObjectBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, BiIntToObjectFunction<LIST> createBuckets) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createBuckets);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createBuckets", createBuckets));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<T, P> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noNode = NO_LONG_NODE;
        final LIST buckets = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                forEach.each(buckets.getValue(node), buckets.getObjectValue(node), parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<T, P, DELEGATE, R> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noNode = NO_LONG_NODE;
        final LIST buckets = getBuckets();

        outer:
            for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

                final long bucketHeadNode = bucketHeadNodesHashArray[i];

                for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                    final R forEachResult = forEach.each(buckets.getValue(node), buckets.getObjectValue(node), parameter, delegate);

                    if (forEachResult != null) {

                        result = forEachResult;

                        break outer;
                    }
                }
            }

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        return result;
    }

    @Override
    public final void keysAndValues(long[] keysDst, T[] valuesDst) {

        final long numElements = getNumElements();

        Checks.isGreaterThanOrEqualTo(keysDst.length, numElements);
        Checks.isGreaterThanOrEqualTo(valuesDst.length, numElements);
        Checks.areEqual(keysDst.length, valuesDst.length);

        if (DEBUG) {

            enter();
        }

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noNode = NO_LONG_NODE;
        final LIST buckets = getBuckets();

        int dstIndex = 0;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                keysDst[dstIndex] = buckets.getValue(node);
                valuesDst[dstIndex] = buckets.getObjectValue(node);

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }
    }

    @Override
    public final T get(long key, T defaultValue) {

        final long node = getValueNode(key);

        return node != NO_LONG_NODE ? getBuckets().getObjectValue(node) : defaultValue;
    }

    @Override
    void rehashPut(LIST oldBuckets, long oldNode, LIST newBuckets, long newNode) {

        if (DEBUG) {

            enter(b -> b.add("oldBuckets", oldBuckets).hex("oldNode", oldNode).add("newBuckets", newBuckets).hex("newNode", newNode));
        }

        final T value = oldBuckets.getObjectValue(oldNode);

        newBuckets.setObjectValue(newNode, value);

        if (DEBUG) {

            exit();
        }
    }

    final void clearBaseLongToObjectBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
