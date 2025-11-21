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
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongKeyBucketMap<
                LIST extends BaseMutableLongLargeMultiHeadSinglyLinkedNodeList<MAP, LIST, VALUES>,
                VALUES extends InheritableLongInnerOuterNodeListValues,
                MAP extends BaseLongKeyBucketMap<LIST, VALUES, MAP>>

        extends BaseIntegerKeyBucketMap<long[], long[], LIST, VALUES, MAP>
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

    abstract void rehashPut(LIST oldBuckets, long oldNode, LIST newBuckets, long newNode);

    BaseLongKeyBucketMap(AllocationType allocationType, int initialCapacityExponent, BiIntToObjectFunction<LIST> createBuckets) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createBuckets);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).addUnordered("createBuckets", createBuckets));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongKeyBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, BiIntToObjectFunction<LIST> createBuckets) {
        this(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT, createBuckets);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).addUnordered("createBuckets", createBuckets));
        }

        if (DEBUG) {

            exit();
        }
    }

    private BaseLongKeyBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent,
            BiIntToObjectFunction<LIST> createBuckets) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, bucketsInnerCapacityExponent, long[]::new, BaseLongKeyBucketMap::clearHashArray,
                createBuckets);
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

        final long result = keysAndValues(addable, null, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> kDst.addInAnyOrder(kSrc[srcIndex]));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected final long[] rehash(long[] hashArray, int newCapacity, int newCapacityExponent, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("keyMask", keyMask));
        }

        final long[] newBucketHeadNodesHashArray = new long[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final LIST oldBuckets = getBuckets();

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LIST newBuckets = createBuckets(newBucketsOuterCapacity, getBucketsInnerCapacity());

        setBuckets(newBuckets);

        final int hashArrayLength = hashArray.length;

        final long noNode = NO_LONG_NODE;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long bucketHeadNode = hashArray[i];

            if (bucketHeadNode != noNode) {

                for (long node = bucketHeadNode; node != noNode; node = oldBuckets.getNextNode(node)) {

                    final long key = oldBuckets.getValue(node);

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

    @Override
    final <KEYS_DST, VALUES_DST> long keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            IIntCapacityMapIndexKeyValueAdder<long[], VALUES, KEYS_DST, VALUES_DST> keyValueAdder) {

        Checks.areAnyNotNull(keysDst, valuesDst);
        Objects.requireNonNull(keyValueAdder);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        final long[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        int numAdded = 0;

        final long noNode = NO_LONG_NODE;

        final LIST b = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = b.getNextNode(node)) {

                keyValueAdder.addValue(numAdded, b, b.getValue(node), getBuckets().get(), valuesDst);

                ++ numAdded;
            }
        }

        if (DEBUG) {

            exit();
        }
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

        checkCapacity(1);

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

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).addUnordered("buckets", buckets).addUnordered("key", key));
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

            exitWithHex(result, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).addUnordered("buckets", buckets).addUnordered("key", key));
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
