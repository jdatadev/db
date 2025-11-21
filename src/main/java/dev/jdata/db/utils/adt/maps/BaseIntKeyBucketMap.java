package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.IntBuckets;
import dev.jdata.db.utils.adt.lists.IIntNodeListValuesMarker;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.IMutableIntSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseIntKeyBucketMap<
                LIST extends IMutableIntSinglyLinkedMultiHeadNodeList<MAP>,
                VALUES extends IIntNodeListValuesMarker,
                MAP extends BaseIntKeyBucketMap<LIST, VALUES, MAP>>

        extends BaseIntegerKeyBucketMap<int[], int[], LIST, VALUES, MAP>
        implements IIntKeyMapCommon, IIntContainsKeyMapGetters {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_BUCKET_MAP;

    private static final ILongNodeSetter<BaseIntKeyBucketMap<?, ?, ?>> headSetter = (i, h) -> i.scratchHashArray[i.scratchHashArrayIndex] = IntBuckets.nodeToInt(h);
    private static final ILongNodeSetter<BaseIntKeyBucketMap<?, ?, ?>> tailSetter = (i, t) -> { };

    private static final long NEW_ADDED_MASK = 1L << 31;

    static boolean isNewAdded(long putResult) {

        return (putResult & NEW_ADDED_MASK) != 0L;
    }

    static long getPutResultNode(long putResult) {

        return (putResult & (~NEW_ADDED_MASK));
    }

    static long makePutResult(long putResult, boolean newAdded) {

        return newAdded ? putResult : (putResult | NEW_ADDED_MASK);
    }

    private int scratchHashArrayIndex;
    private int[] scratchHashArray;

    abstract void rehashPut(LIST oldBuckets, long oldNode, LIST newBuckets, long newNode);

    BaseIntKeyBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent,
            BiIntToObjectFunction<LIST> createBuckets) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, bucketsInnerCapacityExponent, int[]::new, BaseIntKeyBucketMap::clearHashArray,
                createBuckets);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean containsKey(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final boolean found = bucketHeadNode != NO_LONG_NODE && getBuckets().contains(key, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("key", key));
        }

        return found;
    }

    @Override
    public final long keys(IIntAnyOrderAddable addable) {

        Objects.requireNonNull(addable);

        if (DEBUG) {

            enter(b -> b.add("addable", addable));
        }

        final long result = keysAndValues(addable, null, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> kDst.addInAnyOrder(kSrc[srcIndex]));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected final int[] rehash(int[] hashArray, int newCapacity, int newCapacityExponent, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("keyMask", keyMask));
        }

        final int[] newBucketHeadNodesHashArray = new int[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final LIST oldBuckets = getBuckets();
/*
        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LIST newBuckets = createBuckets(newBucketsOuterCapacity, getBucketsInnerCapacity());
*/
        final LIST newBuckets = createBuckets();

        setBuckets(newBuckets);

        final int hashArrayLength = hashArray.length;

        final int noIntNode = IntBuckets.NO_INT_NODE;
        final long noNode = NO_LONG_NODE;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int bucketHeadIntNode = hashArray[i];

            if (bucketHeadIntNode != noIntNode) {

                final long bucketHeadNode = IntBuckets.intToNode(bucketHeadIntNode);

                for (long node = bucketHeadNode; node != noNode; node = oldBuckets.getNextNode(node)) {

                    final int key = oldBuckets.getValue(node);

                    final long putResult = put(newBucketHeadNodesHashArray, newBuckets, key);
                    final long newNode = getPutResultNode(putResult);

                    rehashPut(oldBuckets, node, newBuckets, newNode);
                }
            }
        }

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("keyMask", keyMask));
        }

        return newBucketHeadNodesHashArray;
    }

    private <S, D> void keysAndValues(int[] keysDst, S src, D dst, IIntCapacityMapIndexValueSetter<S, D> valueSetter) {

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final int[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        int dstIndex = 0;

        final long noNode = NO_LONG_NODE;

        final LIST b = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = b.getNextNode(node)) {

                keysDst[dstIndex] = b.getValue(node);

                if (dst != null) {

                    valueSetter.setValue(src, i, dst, dstIndex);
                }

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    final long getValueNode(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final long noNode = NO_LONG_NODE;

        final long result = bucketHeadNode != noNode ? getBuckets().findAtMostOneNode(IntBuckets.intToNode(key), bucketHeadNode) : noNode;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    final long putValueAndReturnNode(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final long result = put(getHashed(), getBuckets(), key);

        if (isNewAdded(result)) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    final long removeElementAndReturnValueNode(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int keyMask = getKeyMask();
        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, keyMask);
        }

        final long removedNode;

        final int[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadIntNode = bucketHeadNodesHashArray[hashArrayIndex];

        final int noIntNode = IntBuckets.NO_INT_NODE;
        final long noNode = NO_LONG_NODE;

        if (bucketHeadIntNode == noIntNode) {

            removedNode = IntBuckets.intToNode(bucketHeadIntNode);
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

            final long bucketHeadNode = IntBuckets.intToNode(bucketHeadIntNode);

            removedNode = getBuckets().removeNodeByValue(mapThis, key, bucketHeadNode, noNode, hSetter, tSetter);
        }

        if (removedNode != noNode) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removedNode);
        }

        return removedNode;
    }

    private long put(int[] bucketHeadNodesHashArray, LIST buckets, int key) {

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

    private static void clearHashArray(int[] bucketHeadNodesHashArray) {

        IntBuckets.clearHashArray(bucketHeadNodesHashArray);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(IElementsView.intNumElements(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final int[] bucketHeadNodesHashArray = getHashed();

        Array.toString(bucketHeadNodesHashArray, 0, bucketHeadNodesHashArray.length, sb, e -> e != IntBuckets.NO_INT_NODE);

        sb.append(']');

        return sb.toString();
    }
}
