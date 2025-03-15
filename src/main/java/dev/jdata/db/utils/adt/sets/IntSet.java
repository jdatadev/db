package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeIntMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class IntSet extends BaseIntegerSet<int[]> implements IIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_INT_SET;

    private static final boolean ASSERT = AssertionContants.ASSERT_INT_SET;

    private static final Class<?> debugClass = IntSet.class;

    private static final int NO_ELEMENT = -1;

//    private static final LongNodeSetter<IntSet> headSetter = (i, h) -> i.scratchSet[i.scratcHashSetIndex] = nodeToInt(h);

    private static final LongNodeSetter<IntSet> headSetter = (i, h) -> {

        final int integer = nodeToInt(h);

        i.scratchSet[i.scratchHashSetIndex] = integer;

        if (DEBUG) {

            printNode(h, i, integer);
        }
    };

    private static final LongNodeSetter<IntSet> tailSetter = (i, t) -> { };

    private static void printNode(long headNode, IntSet intSet, int integer) {

        final StringBuilder sb = new StringBuilder();

        Array.toString(intSet.scratchSet, 0, intSet.scratchSet.length, sb, i -> true, (a, i, b) -> b.append("0x").append(Integer.toHexString(a[i])));

        PrintDebug.formatln(debugClass, "set headnode=0x%016x integer=0x%08x scratchHashSetIndex=%d %s", headNode, integer, intSet.scratchHashSetIndex, sb.toString());
    }

    public static IntSet of(int ... values) {

        return new IntSet(values);
    }

    private final int bucketsInnerCapacity;

    private LargeIntMultiHeadSinglyLinkedList<IntSet> buckets;

    private int scratchHashSetIndex;
    private int[] scratchSet;

    public IntSet(int initialCapacityExponent) {
        this(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public IntSet(int initialCapacityExponent, float loadFactor) {
        this(initialCapacityExponent, loadFactor, BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    private IntSet(int initialCapacityExponent, float loadFactor, int bucketsInnerCapacityExponent) {
        super(initialCapacityExponent, loadFactor, int[]::new, IntSet::clearSet);

        Checks.isCapacityExponent(bucketsInnerCapacityExponent);

        final int bucketsInnerCapacity = CapacityExponents.computeCapacity(bucketsInnerCapacityExponent);

        this.bucketsInnerCapacity = bucketsInnerCapacity;

        this.buckets = new LargeIntMultiHeadSinglyLinkedList<>(BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);
    }

    private IntSet(int[] values) {
        this(computeCapacityExponent(values.length, HashedConstants.DEFAULT_LOAD_FACTOR), HashedConstants.DEFAULT_LOAD_FACTOR);

        for (int value : values) {

            add(value);
        }
    }

    @Override
    public void clear() {

        super.clear();

        buckets.clear();
    }

    @Override
    public boolean contains(int element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int hashSetIndex = HashFunctions.hashIndex(element, getKeyMask());

        final int[] set = getHashed();

        final long bucketHeadNode = intToNode(set[hashSetIndex]);

        final boolean found = bucketHeadNode != BaseList.NO_NODE && buckets.contains(element, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("element", element));
        }

        return found;
    }

    @Override
    public void add(int value) {

        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacity(1);

        final boolean newAdded = add(getHashed(), value);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    public boolean remove(int element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int hashSetIndex = HashFunctions.hashIndex(element, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d element=%d keyMask=0x%08x", hashSetIndex, element, getKeyMask());
        }

        final int[] set = getHashed();

        final long bucketHeadNode = intToNode(set[hashSetIndex]);

        final boolean removed;

        if (bucketHeadNode == BaseList.NO_NODE) {

            removed = false;
        }
        else {
            this.scratchHashSetIndex = hashSetIndex;
            this.scratchSet = set;

            removed = buckets.removeNodeByValue(this, element, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter);
        }

        if (removed) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
    }

    @Override
    protected int[] rehash(int[] set, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("newCapacity", newCapacity));
        }

        final int[] newSet = new int[newCapacity];

        clearSet(newSet);

        final LargeIntMultiHeadSinglyLinkedList<IntSet> oldBuckets = buckets;

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LargeIntMultiHeadSinglyLinkedList<IntSet> newBuckets = new LargeIntMultiHeadSinglyLinkedList<>(newBucketsOuterCapacity, bucketsInnerCapacity);

        this.buckets = newBuckets;

        final int setLength = set.length;

        for (int i = 0; i < setLength; ++ i) {

            final int element = set[i];

            if (element != NO_ELEMENT) {

                for (long node = intToNode(element); node != BaseList.NO_NODE; node = oldBuckets.getNextNode(node)) {

                    add(newSet, oldBuckets.getValue(node));
                }
            }
        }

        if (DEBUG) {

            exit(newSet);
        }

        return newSet;
    }

    private boolean add(int[] set, int value) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("value", value));
        }

        final int hashSetIndex = HashFunctions.hashIndex(value, getKeyMask());

        final long bucketHeadNode = intToNode(set[hashSetIndex]);

        if (DEBUG) {

            debugFormatln("lookup hashSetIndex=%d value=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashSetIndex, value, getKeyMask(), bucketHeadNode);
        }

        final boolean newAdded = bucketHeadNode == BaseList.NO_NODE || !buckets.contains(value, bucketHeadNode);

        final long newBucketHeadNode;

        if (newAdded) {

            this.scratchHashSetIndex = hashSetIndex;
            this.scratchSet = set;

            newBucketHeadNode = buckets.addHead(this, value, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter);
        }
        else {
            newBucketHeadNode = BaseList.NO_NODE;
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("set", set).add("value", value).hex("newBucketHeadNode", newBucketHeadNode));
        }

        return newAdded;
    }

    private static void clearSet(int[] set) {

        Arrays.fill(set, NO_ELEMENT);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final int[] set = getHashed();

        Array.toString(set, 0, set.length, sb, e -> e != NO_ELEMENT);

        sb.append(']');

        return sb.toString();
    }

    @Deprecated
    private static int nodeToInt(long node) {

        return node != BaseList.NO_NODE
                ? (Integers.checkUnsignedLongToUnsignedShort(node >>> 32) << 16) | Integers.checkUnsignedLongToUnsignedShort(node & 0xFFFFFFFFL)
                : (int)BaseList.NO_NODE;
    }

    @Deprecated
    private static long intToNode(int integer) {

        return integer != BaseList.NO_NODE
                ? (((long)integer & 0xFFFF0000) << 16) | (integer & 0x0000FFFF)
                : BaseList.NO_NODE;
    }
}
