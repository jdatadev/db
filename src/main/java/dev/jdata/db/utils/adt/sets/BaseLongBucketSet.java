package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongElements;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongBucketSet extends BaseIntegerBucketSet<long[]> implements ILongSetGetters, ILongElements {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_BUCKET_SET;

//    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_LONG_BUCKET_SET;

    private static final long NO_LONG_NODE = BaseList.NO_NODE;

    private static final LongNodeSetter<BaseLongBucketSet> headSetter = (i, h) -> i.scratchHashArray[i.scratchHashArrayIndex] = h;
    private static final LongNodeSetter<BaseLongBucketSet> tailSetter = (i, t) -> { };

    private final int bucketsInnerCapacity;

    private LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> buckets;

    private int scratchHashArrayIndex;
    private long[] scratchHashArray;

    BaseLongBucketSet() {
        this(3);
    }

    BaseLongBucketSet(int initialCapacityExponent) {
        this(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    BaseLongBucketSet(int initialCapacityExponent, float loadFactor) {
        this(initialCapacityExponent, loadFactor, BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    private BaseLongBucketSet(int initialCapacityExponent, float loadFactor, int bucketsInnerCapacityExponent) {
        super(initialCapacityExponent, loadFactor, long[]::new, BaseLongBucketSet::clearHashArray);

        Checks.isCapacityExponent(bucketsInnerCapacityExponent);

        final int bucketsInnerCapacity = CapacityExponents.computeCapacity(bucketsInnerCapacityExponent);

        this.bucketsInnerCapacity = bucketsInnerCapacity;

        this.buckets = new LargeLongMultiHeadSinglyLinkedList<>(BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);
    }

    BaseLongBucketSet(long[] values) {
        this(computeCapacityExponent(values.length, HashedConstants.DEFAULT_LOAD_FACTOR), HashedConstants.DEFAULT_LOAD_FACTOR);

        for (long value : values) {

            addValue(value);
        }
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> b = buckets;

        final long noNode = BaseList.NO_NODE;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            for (long n = bucketHeadNodesHashArray[i]; n != noNode; n = b.getNextNode(n)) {

                forEach.each(b.getValue(n), parameter);
            }
        }
    }

    @Override
    public final boolean contains(long value) {

        if (DEBUG) {

            enter(b -> b.add("element", value));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, getKeyMask());

        final long[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final boolean found = bucketHeadNode != BaseList.NO_NODE && buckets.contains(value, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("element", value));
        }

        return found;
    }

    @Override
    protected final long[] rehash(long[] hashArray, int newCapacity, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
        }

        final long[] newBucketHeadNodesHashArray = new long[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> oldBuckets = buckets;

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> newBuckets = new LargeLongMultiHeadSinglyLinkedList<>(newBucketsOuterCapacity, bucketsInnerCapacity);

        this.buckets = newBuckets;

        final int hashArrayLength = hashArray.length;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long bucketHeadNode = hashArray[i];

            if (bucketHeadNode != NO_LONG_NODE) {

                for (long node = bucketHeadNode; node != BaseList.NO_NODE; node = oldBuckets.getNextNode(node)) {

                    add(newBucketHeadNodesHashArray, oldBuckets.getValue(node), keyMask);
                }
            }
        }

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
        }

        return newBucketHeadNodesHashArray;
    }

    final boolean addValue(long value) {

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

    final boolean removeElement(long element) {

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int keyMask = getKeyMask();
        final int hashArrayIndex = HashFunctions.hashArrayIndex(element, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, element, keyMask);
        }

        final long bucketHeadNode = getHashed()[hashArrayIndex];

        final boolean removed;

        if (bucketHeadNode == BaseList.NO_NODE) {

            removed = false;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;

            removed = buckets.removeNodeByValue(this, element, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter) != BaseList.NO_NODE;
        }

        if (removed) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
    }

    final void clearBaseLongBucketSet() {

        clearHashed();

        buckets.clear();
    }

    private boolean add(long[] bucketHeadNodesHashArray, long value, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("value", value).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, keyMask);

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d value=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashArrayIndex, value, keyMask, bucketHeadNode);
        }

        final boolean newAdded = bucketHeadNode == BaseList.NO_NODE || !buckets.contains(value, bucketHeadNode);

        if (newAdded) {

            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            buckets.addHead(this, value, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter);
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("value", value).hex("keyMask", keyMask));
        }

        return newAdded;
    }

    private static void clearHashArray(long[] bucketHeadNodesHashArray) {

        Arrays.fill(bucketHeadNodesHashArray, NO_LONG_NODE);
    }

    @Override
    public final String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final long[] bucketHeadNodesHashArray = getHashed();

        Array.toString(bucketHeadNodesHashArray, 0, bucketHeadNodesHashArray.length, sb, e -> e != NO_LONG_NODE);

        sb.append(']');

        return sb.toString();
    }
}
