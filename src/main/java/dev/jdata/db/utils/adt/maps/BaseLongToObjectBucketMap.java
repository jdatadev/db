package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongToObjectBucketMap<

                V,
                LIST extends BaseLongToObjectBucketMapMultiHeadSinglyLinkedNodeList<MAP, V, LIST, VALUES>,
                VALUES extends BaseLongToObjectValues<MAP, V, LIST, VALUES>,
                MAP extends BaseLongToObjectBucketMap<V, LIST, VALUES, MAP>>

        extends BaseLongKeyBucketMap<LIST, VALUES, MAP>
        implements ILongToObjectDynamicMapCommon<V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_BUCKET_MAP;

    BaseLongToObjectBucketMap(AllocationType allocationType, int initialCapacityExponent, BiIntToObjectFunction<LIST> createBuckets) {
        super(allocationType, initialCapacityExponent, createBuckets);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createBuckets", createBuckets));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongToObjectBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, BiIntToObjectFunction<LIST> createBuckets) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createBuckets);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createBuckets", createBuckets));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P, E extends Exception> void forEachKeyAndValue(P parameter, ILongToObjectForEachMapKeyAndValue<V, P, E> forEach) throws E {

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
    public final <P1, P2, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2,
            ILongToObjectForEachMapKeyAndValueWithResult<V, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
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

                    final R forEachResult = forEach.each(buckets.getValue(node), buckets.getObjectValue(node), parameter1, parameter2);

                    if (forEachResult != null) {

                        result = forEachResult;

                        break outer;
                    }
                }
            }

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        return result;
    }

    @Override
    public final long keysAndValues(ILongAnyOrderAddable keysDst, IObjectAnyOrderAddable<V> valuesDst) {

        Objects.requireNonNull(keysDst);
        Objects.requireNonNull(valuesDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noNode = NO_LONG_NODE;
        final LIST buckets = getBuckets();

        int numAdded = 0;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                keysDst.addInAnyOrder(buckets.getValue(node));
                valuesDst.addInAnyOrder(buckets.getObjectValue(node));

                ++ numAdded;
            }
        }

        if (DEBUG) {

            exit(numAdded, b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }

        return numAdded;
    }

    @Override
    public final V get(long key, V defaultValue) {

        final long node = getValueNode(key);

        return node != NO_LONG_NODE ? getBuckets().getObjectValue(node) : defaultValue;
    }

    @Override
    void rehashPut(LIST oldBuckets, long oldNode, LIST newBuckets, long newNode) {

        if (DEBUG) {

            enter(b -> b.add("oldBuckets", oldBuckets).hex("oldNode", oldNode).add("newBuckets", newBuckets).hex("newNode", newNode));
        }

        final V value = oldBuckets.getObjectValue(oldNode);

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
