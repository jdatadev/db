package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.Buckets;
import dev.jdata.db.utils.adt.lists.BaseIntValues;
import dev.jdata.db.utils.adt.lists.BaseLargeIntMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.BaseLargeList.BaseValuesFactory;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.function.BiIntToObjectFunction;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntBucketMap<
                LIST extends BaseLargeIntMultiHeadSinglyLinkedList<MAP, LIST, VALUES>,
                VALUES extends BaseIntValues<LIST, VALUES>,
                MAP extends BaseIntBucketMap<LIST, VALUES, MAP>>

        extends BaseIntegerBucketMap<int[], int[], LIST, VALUES, MAP>
        implements IIntKeyMap, IIntContainsKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_BUCKET_MAP;

    private static final LongNodeSetter<BaseIntBucketMap<?, ?, ?>> headSetter = (i, h) -> i.scratchHashArray[i.scratchHashArrayIndex] = Buckets.nodeToInt(h);
    private static final LongNodeSetter<BaseIntBucketMap<?, ?, ?>> tailSetter = (i, t) -> { };

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

    BaseIntBucketMap(int initialCapacityExponent, BiIntToObjectFunction<LIST> createBuckets, BaseValuesFactory<int[], LIST, VALUES> valuesFactory) {
        this(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR, createBuckets, valuesFactory);
    }

    BaseIntBucketMap(int initialCapacityExponent, float loadFactor, BiIntToObjectFunction<LIST> createBuckets, BaseValuesFactory<int[], LIST, VALUES> valuesFactory) {
        this(initialCapacityExponent, loadFactor, BUCKETS_INNER_CAPACITY_EXPONENT, createBuckets, valuesFactory);
    }

    private BaseIntBucketMap(int initialCapacityExponent, float loadFactor, int bucketsInnerCapacityExponent, BiIntToObjectFunction<LIST> createBuckets,
            BaseValuesFactory<int[], LIST, VALUES> valuesFactory) {
        super(initialCapacityExponent, loadFactor, bucketsInnerCapacityExponent, int[]::new, BaseIntBucketMap::clearHashArray, createBuckets, valuesFactory);
    }

    @Override
    public final boolean containsKey(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final boolean found = bucketHeadNode != BaseList.NO_NODE && getBuckets().contains(key, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("key", key));
        }

        return found;
    }

    @Override
    public final int[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final int[] result = new int[numElements];

        keys(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected final int[] rehash(int[] hashArray, int newCapacity, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
        }

        final int[] newBucketHeadNodesHashArray = new int[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final LIST oldBuckets = getBuckets();

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LIST newBuckets = createBuckets(newBucketsOuterCapacity, getBucketsInnerCapacity());

        setBuckets(newBuckets);

        final int hashArrayLength = hashArray.length;

        final int noIntNode = Buckets.NO_INT_NODE;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int bucketHeadIntNode = hashArray[i];

            if (bucketHeadIntNode != noIntNode) {

                final long bucketHeadNode = Buckets.intToNode(bucketHeadIntNode);

                for (long node = bucketHeadNode; node != BaseList.NO_NODE; node = oldBuckets.getNextNode(node)) {

                    put(newBucketHeadNodesHashArray, oldBuckets.getValue(node));
                }
            }
        }

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
        }

        return newBucketHeadNodesHashArray;
    }

    private void keys(int[] dst) {

        keysAndValues(dst, null, null, null);
    }

    private <S, D> void keysAndValues(int[] keysDst, S src, D dst, ValueSetter<S, D> valueSetter) {

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final int[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        int dstIndex = 0;

        final long noNode = BaseList.NO_NODE;

        final LIST b = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            if (bucketHeadNode != noNode) {

                for (long node = bucketHeadNode; node != noNode; node = b.getNextNode(node)) {

                    keysDst[dstIndex] = Buckets.nodeToInt(node);

                    if (dst != null) {

                        valueSetter.setValue(src, i, dst, dstIndex);
                    }

                    ++ dstIndex;
                }
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    private long getValueNode(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final long noNode = BaseList.NO_NODE;

        final long result = bucketHeadNode != noNode ? getBuckets().findNodeWithValue(Buckets.nodeToInt(key), bucketHeadNode) : noNode;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    private long putValueAndReturnNode(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final long result = put(getHashed(), key);

        if (isNewAdded(result)) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    private long removeElementAndReturnValueNode(int key) {

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

        final int noIntNode = Buckets.NO_INT_NODE;
        final long noNode = BaseList.NO_NODE;

        if (bucketHeadIntNode == noIntNode) {

            removedNode = Buckets.intToNode(bucketHeadIntNode);
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            @SuppressWarnings("unchecked")
            final MAP mapThis = (MAP)this;

            @SuppressWarnings("unchecked")
            final LongNodeSetter<MAP> hSetter = (LongNodeSetter<MAP>)headSetter;

            @SuppressWarnings("unchecked")
            final LongNodeSetter<MAP> tSetter = (LongNodeSetter<MAP>)tailSetter;

            final long bucketHeadNode = Buckets.intToNode(bucketHeadIntNode);

            removedNode = getBuckets().removeNodeByValue(mapThis, key, bucketHeadNode, noNode, hSetter, tSetter);
        }

        if (removedNode == noNode) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removedNode);
        }

        return removedNode;
    }

    private long put(int[] bucketHeadNodesHashArray, int key) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("key", key));
        }

        final int keyMask = getKeyMask();
        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashArrayIndex, key, keyMask, bucketHeadNode);
        }

        final LIST buckets = getBuckets();

        final long result;

        final long previousNode = buckets.findNodeWithValue(key, bucketHeadNode);

        if (previousNode != BaseList.NO_NODE) {

            result = makePutResult(previousNode, false);
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            @SuppressWarnings("unchecked")
            final MAP mapThis = (MAP)this;

            @SuppressWarnings("unchecked")
            final LongNodeSetter<MAP> hSetter = (LongNodeSetter<MAP>)headSetter;

            @SuppressWarnings("unchecked")
            final LongNodeSetter<MAP> tSetter = (LongNodeSetter<MAP>)tailSetter;

            final long node = buckets.addHead(mapThis, key, bucketHeadNode, BaseList.NO_NODE, hSetter, tSetter);

            result = makePutResult(node, true);
        }

        if (DEBUG) {

            exit(result, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("key", key));
        }

        return result;
    }

    private static void clearHashArray(int[] bucketHeadNodesHashArray) {

        Arrays.fill(bucketHeadNodesHashArray, Buckets.NO_INT_NODE);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final int[] bucketHeadNodesHashArray = getHashed();

        Array.toString(bucketHeadNodesHashArray, 0, bucketHeadNodesHashArray.length, sb, e -> e != Buckets.NO_INT_NODE);

        sb.append(']');

        return sb.toString();
    }
}
