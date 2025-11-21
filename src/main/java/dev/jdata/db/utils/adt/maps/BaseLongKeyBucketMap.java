package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.LongBuckets;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.InheritableLongInnerOuterNodeListValues;
import dev.jdata.db.utils.adt.lists.InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongKeyBucketMap<

                LIST extends InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList<MAP, VALUES>,
                VALUES extends InheritableLongInnerOuterNodeListValues,
                MAP extends BaseLongKeyBucketMap<LIST, VALUES, MAP>>

        extends BaseBucketMap<long[], long[], LIST, VALUES, MAP>
        implements ILongKeyMapCommon, ILongContainsKeyMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_BUCKET_MAP;

    private static final ILongNodeSetter<BaseLongKeyBucketMap<?, ?, ?>> headSetter = (i, h) -> i.scratchHashArray[i.scratchHashArrayIndex] = h;
    private static final ILongNodeSetter<BaseLongKeyBucketMap<?, ?, ?>> tailSetter = (i, t) -> { };

    private static final long NEW_ADDED_MASK = 1L << 63;

    static boolean isNewAdded(long putResult) {

        return (putResult & NEW_ADDED_MASK) != 0L;
    }

    static long getPutResultNode(long putResult) {

        return (putResult & (~NEW_ADDED_MASK));
    }

    static long makePutResult(long putResult, boolean newAdded) {

        return newAdded ? (putResult | NEW_ADDED_MASK) : putResult;
    }

    private int scratchHashArrayIndex;
    private long[] scratchHashArray;

    BaseLongKeyBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent,
            BiIntToObjectFunction<LIST> createBuckets) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, bucketsInnerCapacityExponent, long[]::new, BaseLongKeyBucketMap::clearHashArray,
                createBuckets);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent).add("createBuckets", createBuckets));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean containsKey(long key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final boolean result = getValueNode(key) != NO_LONG_NODE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final long keys(ILongAnyOrderAddable addable) {

        Objects.requireNonNull(addable);

        if (DEBUG) {

            enter(b -> b.add("addable", addable));
        }

        final long result = keysAndValues(addable, null, (srcNode, srcBuckets, dstIndex, keysDst, valuesDst) -> keysDst.addInAnyOrder(srcBuckets.getValue(srcNode)));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected final void resetToNull() {

        super.resetToNull();

        this.scratchHashArray = null;
    }

    @Override
    final void rehashBuckets(long[] hashArray, long[] newHashArray, int newKeyMask, LIST buckets, LIST newBuckets) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("buckets", buckets).add("newBuckets", newBuckets));
        }

        final int hashArrayLength = hashArray.length;

        final long noNode = NO_LONG_NODE;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long bucketHeadNode = hashArray[i];

            if (bucketHeadNode != noNode) {

                for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                    final long key = buckets.getValue(node);

                    final long putResult = put(newHashArray, newBuckets, key);
                    final long newNode = getPutResultNode(putResult);

                    rehashPut(buckets, node, newBuckets, newNode);
                }
            }
        }

        if (DEBUG) {

            exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("buckets", buckets).add("newBuckets", newBuckets));
        }
    }

    @Override
    final <KEYS_DST, VALUES_DST> int keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst, IIntCapacityBucketMapKeyValueAdder<LIST, KEYS_DST, VALUES_DST> keyValueAdder) {

        Checks.areAnyNotNull(keysDst, valuesDst);
        Objects.requireNonNull(keyValueAdder);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;
        final LIST buckets = getBuckets();

        final long noNode = NO_LONG_NODE;

        int numAdded = 0;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                keyValueAdder.addValue(node, buckets, numAdded, keysDst, valuesDst);

                ++ numAdded;
            }
        }

        if (DEBUG) {

            exit(numAdded);
        }

        return numAdded;
    }

    final long getValueNode(long key) {

        LongBuckets.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final long[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final long noNode = NO_LONG_NODE;

        final long result = bucketHeadNode != noNode ? getBuckets().findAtMostOneNode(key, bucketHeadNode) : noNode;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    final long putValueAndReturnNode(long key) {

        LongBuckets.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacityForOneMoreElement();

        final long putResult = put(getHashed(), getBuckets(), key);

        if (isNewAdded(putResult)) {

            incrementNumElements();
        }

        if (DEBUG) {

            exitWithHex(putResult, b -> b.add("key", key));
        }

        return putResult;
    }

    final long removeElementAndReturnValueNode(long key) {

        LongBuckets.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int keyMask = getKeyMask();
        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, keyMask);
        }

        final long removedNode;

        final long[] bucketHeadNodesHashArray = getHashed();
        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final long noNode = NO_LONG_NODE;

        if (bucketHeadNode == noNode) {

            removedNode = noNode;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            @SuppressWarnings("unchecked")
            final MAP mapThis = (MAP)this;

            @SuppressWarnings("unchecked")
            final ILongNodeSetter<MAP> hSetter = (ILongNodeSetter<MAP>)headSetter;

            @SuppressWarnings("unchecked")
            final ILongNodeSetter<MAP> tSetter = (ILongNodeSetter<MAP>)tailSetter;

            removedNode = getBuckets().removeAtMostOneNodeByValue(mapThis, key, bucketHeadNode, noNode, hSetter, tSetter);
        }

        if (removedNode != noNode) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removedNode);
        }

        return removedNode;
    }

    private long put(long[] bucketHeadNodesHashArray, LIST buckets, long key) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("buckets", buckets).add("key", key));
        }

        final int keyMask = getKeyMask();
        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashArrayIndex, key, keyMask, bucketHeadNode);
        }

        final long result;

        final long previousNode = buckets.findAtMostOneNode(key, bucketHeadNode);

        final long noNode = NO_LONG_NODE;

        if (previousNode != noNode) {

            result = makePutResult(previousNode, false);
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            @SuppressWarnings("unchecked")
            final MAP mapThis = (MAP)this;

            @SuppressWarnings("unchecked")
            final ILongNodeSetter<MAP> hSetter = (ILongNodeSetter<MAP>)headSetter;

            @SuppressWarnings("unchecked")
            final ILongNodeSetter<MAP> tSetter = (ILongNodeSetter<MAP>)tailSetter;

            final long node = buckets.addHead(mapThis, key, bucketHeadNode, noNode, hSetter, tSetter);

            result = makePutResult(node, true);
        }

        if (DEBUG) {

            exitWithHex(result, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("buckets", buckets).add("key", key));
        }

        return result;
    }

    private static void clearHashArray(long[] bucketHeadNodesHashArray) {

        LongBuckets.clearHashArray(bucketHeadNodesHashArray);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(IOnlyElementsView.intNumElements(this) * 10);

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final long[] bucketHeadNodesHashArray = getHashed();

        Array.toString(bucketHeadNodesHashArray, 0, bucketHeadNodesHashArray.length, sb, e -> e != NO_LONG_NODE);

        sb.append(']');

        return sb.toString();
    }
}
