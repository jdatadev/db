package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.adt.lists.BaseLargeList.BaseValuesFactory;
import dev.jdata.db.utils.adt.lists.BaseLargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.BaseLongValues;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.function.BiIntToObjectFunction;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongKeyBucketMap<
                LIST extends BaseLargeLongMultiHeadSinglyLinkedList<MAP, LIST, VALUES>,
                VALUES extends BaseLongValues<LIST, VALUES>,
                MAP extends BaseLongKeyBucketMap<LIST, VALUES, MAP>>

        extends BaseIntegerKeyBucketMap<long[], long[], LIST, VALUES, MAP>
        implements ILongKeyMap, ILongContainsKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_BUCKET_MAP;

    private static final LongNodeSetter<BaseLongKeyBucketMap<?, ?, ?>> headSetter = (i, h) -> i.scratchHashArray[i.scratchHashArrayIndex] = h;
    private static final LongNodeSetter<BaseLongKeyBucketMap<?, ?, ?>> tailSetter = (i, t) -> { };

    private static final long NEW_ADDED_MASK = 1L << 63;

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
    private long[] scratchHashArray;

    BaseLongKeyBucketMap(int initialCapacityExponent, BiIntToObjectFunction<LIST> createBuckets, BaseValuesFactory<long[], LIST, VALUES> valuesFactory) {
        this(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR, createBuckets, valuesFactory);
    }

    BaseLongKeyBucketMap(int initialCapacityExponent, float loadFactor, BiIntToObjectFunction<LIST> createBuckets, BaseValuesFactory<long[], LIST, VALUES> valuesFactory) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, BUCKETS_INNER_CAPACITY_EXPONENT, createBuckets, valuesFactory);
    }

    BaseLongKeyBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, BiIntToObjectFunction<LIST> createBuckets,
            BaseValuesFactory<long[], LIST, VALUES> valuesFactory) {
        this(initialCapacityExponent, capacityExponentIncrease, loadFactor, BUCKETS_INNER_CAPACITY_EXPONENT, createBuckets, valuesFactory);
    }

    private BaseLongKeyBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent,
            BiIntToObjectFunction<LIST> createBuckets, BaseValuesFactory<long[], LIST, VALUES> valuesFactory) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, bucketsInnerCapacityExponent, long[]::new, BaseLongKeyBucketMap::clearHashArray, createBuckets,
                valuesFactory);
    }

    @Override
    public final boolean containsKey(long key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = IntPutResult.getPutIndex(key);

        final boolean result = index != NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final long[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final long[] result = new long[numElements];

        keys(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected final long[] rehash(long[] hashArray, int newCapacity, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
        }

        final long[] newBucketHeadNodesHashArray = new long[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final LIST oldBuckets = getBuckets();

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LIST newBuckets = createBuckets(newBucketsOuterCapacity, getBucketsInnerCapacity());

        setBuckets(newBuckets);

        final int hashArrayLength = hashArray.length;

        final long noNode = BaseList.NO_NODE;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long bucketHeadNode = hashArray[i];

            if (bucketHeadNode != noNode) {

                for (long node = bucketHeadNode; node != noNode; node = oldBuckets.getNextNode(node)) {

                    put(newBucketHeadNodesHashArray, oldBuckets.getValue(node));
                }
            }
        }

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
        }

        return newBucketHeadNodesHashArray;
    }

    final <S, D> void keysAndValues(long[] keysDst, S src, D dst, MapIndexValueSetter<S, D> valueSetter) {

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final long[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        int dstIndex = 0;

        final long noNode = BaseList.NO_NODE;

        final LIST b = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            if (bucketHeadNode != noNode) {

                for (long node = bucketHeadNode; node != BaseList.NO_NODE; node = b.getNextNode(node)) {

                    keysDst[dstIndex] = bucketHeadNode;

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

    final long getValueNode(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final long[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final long result = bucketHeadNode != BaseList.NO_NODE ? getBuckets().findNodeWithValue(key, bucketHeadNode) : BaseList.NO_NODE;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    final long putValueAndReturnNode(long key) {

        NonBucket.checkIsHashArrayElement(key);

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

    final long removeElementAndReturnValueNode(long key) {

        NonBucket.checkIsHashArrayElement(key);

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

        if (bucketHeadNode == BaseList.NO_NODE) {

            removedNode = BaseList.NO_NODE;
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

            removedNode = getBuckets().removeNodeByValue(mapThis, key, bucketHeadNode, BaseList.NO_NODE, hSetter, tSetter);
        }

        if (removedNode == BaseList.NO_NODE) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removedNode);
        }

        return removedNode;
    }

    private long put(long[] bucketHeadNodesHashArray, long key) {

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

    private void keys(long[] dst) {

        keysAndValues(dst, null, null, null);
    }

    private static void clearHashArray(long[] bucketHeadNodesHashArray) {

        Arrays.fill(bucketHeadNodesHashArray, BaseList.NO_NODE);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final long[] bucketHeadNodesHashArray = getHashed();

        Array.toString(bucketHeadNodesHashArray, 0, bucketHeadNodesHashArray.length, sb, e -> e != BaseList.NO_NODE);

        sb.append(']');

        return sb.toString();
    }
}
