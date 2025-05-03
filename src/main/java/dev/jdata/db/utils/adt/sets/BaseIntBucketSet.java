package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.Buckets;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeIntMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.adt.strings.StringBuilders;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntBucketSet extends BaseIntegerBucketSet<int[]> implements IIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_BUCKET_SET;

//    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_INT_BUCKET_SET;

    private static final Class<?> debugClass = MutableIntBucketSet.class;

//    private static final LongNodeSetter<BaseIntBucketSet> headSetter = (i, h) -> i.scratchSet[i.scratcHashArrayIndex] = nodeToInt(h);

    private static final LongNodeSetter<BaseIntBucketSet> headSetter = (i, h) -> {

        final int integer = Buckets.nodeToInt(h);

        i.scratchHashArray[i.scratchHashArrayIndex] = integer;

        if (DEBUG) {

            printNode(h, i, integer);
        }
    };

    private static final LongNodeSetter<BaseIntBucketSet> tailSetter = (i, t) -> { };

    private static void printNode(long bucketHeadNode, BaseIntBucketSet intBucketSet, int integer) {

        final StringBuilder sb = new StringBuilder();

        Array.toString(intBucketSet.scratchHashArray, 0, intBucketSet.scratchHashArray.length, sb, i -> true, (a, i, b) -> StringBuilders.hexString(b, a[i], true));

        PrintDebug.formatln(debugClass, "set bucketHeadnode=0x%016x integer=0x%08x scratchHashArrayIndex=%d %s", bucketHeadNode, integer, intBucketSet.scratchHashArrayIndex,
                sb.toString());
    }

    private final int bucketsInnerCapacity;

    private LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> buckets;

    private int scratchHashArrayIndex;
    private int[] scratchHashArray;

    BaseIntBucketSet(int initialCapacityExponent) {
        this(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    BaseIntBucketSet(int initialCapacityExponent, float loadFactor) {
        this(initialCapacityExponent, loadFactor, BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    private BaseIntBucketSet(int initialCapacityExponent, float loadFactor, int bucketsInnerCapacityExponent) {
        super(initialCapacityExponent, loadFactor, int[]::new, BaseIntBucketSet::clearHashArray);

        Checks.isCapacityExponent(bucketsInnerCapacityExponent);

        final int bucketsInnerCapacity = CapacityExponents.computeCapacity(bucketsInnerCapacityExponent);

        this.bucketsInnerCapacity = bucketsInnerCapacity;

        this.buckets = new LargeIntMultiHeadSinglyLinkedList<>(BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);
    }

    BaseIntBucketSet(int[] values) {
        this(computeCapacityExponent(values.length, HashedConstants.DEFAULT_LOAD_FACTOR), HashedConstants.DEFAULT_LOAD_FACTOR);

        for (int value : values) {

            addValue(value);
        }
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final int[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> b = buckets;

        final int noIntNode = Buckets.NO_INT_NODE;
        final long noNode = BaseList.NO_NODE;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final int bucketHeadIntNode = bucketHeadNodesHashArray[i];

            if (bucketHeadIntNode != noIntNode) {

                final long bucketHeadNode = Buckets.intToNode(bucketHeadIntNode);

                for (long n = bucketHeadNode; n != noNode; n = b.getNextNode(n)) {

                    forEach.each(b.getValue(n), parameter);
                }
            }
        }
    }

    @Override
    public final boolean contains(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, getKeyMask());

        final int[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = Buckets.intToNode(bucketHeadNodesHashArray[hashArrayIndex]);

        final boolean found = bucketHeadNode != BaseList.NO_NODE && buckets.contains(value, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("value", value));
        }

        return found;
    }

    @Override
    protected final int[] rehash(int[] hashArray, int newCapacity, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
        }

        final int[] newBucketHeadNodesHashArray = new int[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> oldBuckets = buckets;

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> newBuckets = new LargeIntMultiHeadSinglyLinkedList<>(newBucketsOuterCapacity, bucketsInnerCapacity);

        this.buckets = newBuckets;

        final int noIntNode = Buckets.NO_INT_NODE;
        final long noNode = BaseList.NO_NODE;

        final int setLength = hashArray.length;

        for (int i = 0; i < setLength; ++ i) {

            final int bucketHeadIntNode = hashArray[i];

            if (bucketHeadIntNode != noIntNode) {

                final long bucketHeadNode = Buckets.intToNode(bucketHeadIntNode);

                for (long node = bucketHeadNode; node != noNode; node = oldBuckets.getNextNode(node)) {

                    add(newBucketHeadNodesHashArray, oldBuckets.getValue(node), keyMask);
                }
            }
        }

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
        }

        return newBucketHeadNodesHashArray;
    }

    final void clearBaseIntBucketSet() {

        clearHashed();

        buckets.clear();
    }

    final boolean removeElement(int value) {

        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int keyMask = getKeyMask();
        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d value=%d keyMask=0x%08x", hashArrayIndex, value, keyMask);
        }

        final int[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = Buckets.intToNode(bucketHeadNodesHashArray[hashArrayIndex]);

        final long noNode = BaseList.NO_NODE;

        final boolean removed;

        if (bucketHeadNode == noNode) {

            removed = false;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            removed = buckets.removeNodeByValue(this, value, bucketHeadNode, noNode, headSetter, tailSetter) != noNode;
        }

        if (removed) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
    }

    final boolean addValue(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacity(1);

        final boolean newAdded = add(getHashed(), value, getKeyMask());

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("value", value));
        }

        return newAdded;
    }

    private boolean add(int[] bucketHeadNodesHashArray, int value, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("value", value).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, keyMask);

        final long bucketHeadNode = Buckets.intToNode(bucketHeadNodesHashArray[hashArrayIndex]);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d value=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashArrayIndex, value, keyMask, bucketHeadNode);
        }

        final long noNode = BaseList.NO_NODE;

        final boolean newAdded = bucketHeadNode == noNode || !buckets.contains(value, bucketHeadNode);

        final long newBucketHeadNode;

        if (newAdded) {

            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            newBucketHeadNode = buckets.addHead(this, value, bucketHeadNode, noNode, headSetter, tailSetter);
        }
        else {
            newBucketHeadNode = noNode;
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("value", value).hex("newBucketHeadNode", newBucketHeadNode));
        }

        return newAdded;
    }

    @Override
    public final String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final int[] bucketHeadNodesHashArray = getHashed();

        Array.toString(bucketHeadNodesHashArray, 0, bucketHeadNodesHashArray.length, sb, e -> e != Buckets.NO_INT_NODE);

        sb.append(']');

        return sb.toString();
    }

    private static void clearHashArray(int[] bucketHeadNodesHashArray) {

        Arrays.fill(bucketHeadNodesHashArray, Buckets.NO_INT_NODE);
    }
}
