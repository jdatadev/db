package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongIterableElements;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.LongBuckets;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongBucketSet extends BaseIntegerBucketSet<long[]> implements ILongSetGetters, ILongIterableElements {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_BUCKET_SET;

    private static final ILongNodeSetter<BaseLongBucketSet> headSetter = (i, h) -> i.scratchHashArray[i.scratchHashArrayIndex] = h;
    private static final ILongNodeSetter<BaseLongBucketSet> tailSetter = (i, t) -> { };

    private final int bucketsInnerCapacity;

    private LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> buckets;

    private int scratchHashArrayIndex;
    private long[] scratchHashArray;

    BaseLongBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new, BaseLongBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent));
        }

        Checks.isIntCapacityExponent(bucketsInnerCapacityExponent);

        final int bucketsInnerCapacity = CapacityExponents.computeIntCapacityFromExponent(bucketsInnerCapacityExponent);

        this.bucketsInnerCapacity = bucketsInnerCapacity;

        this.buckets = new LargeLongMultiHeadSinglyLinkedList<>(DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);

        if (DEBUG) {

            exit();
        }
    }

    BaseLongBucketSet(long[] values) {
        this(computeRehashCapacityExponent(values.length, DEFAULT_LOAD_FACTOR), DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("values", values));
        }

        for (long value : values) {

            addValue(value);
        }

        if (DEBUG) {

            exit();
        }
    }

    private BaseLongBucketSet(int initialCapacityExponent, float loadFactor) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> b = buckets;

        final long noNode = NO_LONG_NODE;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            for (long node = bucketHeadNodesHashArray[i]; node != noNode; node = b.getNextNode(node)) {

                forEach.each(b.getValue(node), parameter);
            }
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final long[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> b = buckets;

        final long noNode = NO_LONG_NODE;

        outer:
            for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

                for (long node = bucketHeadNodesHashArray[i]; node != noNode; node = b.getNextNode(node)) {

                    final R forEachResult = forEach.each(b.getValue(node), parameter1, parameter2);

                    if (forEachResult != null) {

                        result = forEachResult;

                        break outer;
                    }
                }
            }

        return result;
    }

    @Override
    public final boolean contains(long value) {

        if (DEBUG) {

            enter(b -> b.add("element", value));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, getKeyMask());

        final long[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodesHashArray[hashArrayIndex];

        final boolean found = bucketHeadNode != NO_LONG_NODE && buckets.contains(value, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("element", value));
        }

        return found;
    }

    @Override
    protected final long[] rehash(long[] hashArray, int newCapacity, int newCapacityExponent, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("keyMask", keyMask));
        }

        final long[] newBucketHeadNodesHashArray = new long[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> oldBuckets = buckets;

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LargeLongMultiHeadSinglyLinkedList<BaseLongBucketSet> newBuckets = new LargeLongMultiHeadSinglyLinkedList<>(newBucketsOuterCapacity, bucketsInnerCapacity);

        this.buckets = newBuckets;

        final int hashArrayLength = hashArray.length;

        final long noNode = NO_LONG_NODE;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long bucketHeadNode = hashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = oldBuckets.getNextNode(node)) {

                add(newBucketHeadNodesHashArray, oldBuckets.getValue(node), keyMask);
            }
        }

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent)
                    .hex("keyMask", keyMask));
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

        final boolean removed;

        final int keyMask = getKeyMask();
        final int hashArrayIndex = HashFunctions.hashArrayIndex(element, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, element, keyMask);
        }

        final long bucketHeadNode = getHashed()[hashArrayIndex];

        final long noNode = NO_LONG_NODE;

        if (bucketHeadNode == noNode) {

            removed = false;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;

            removed = buckets.removeAtMostOneNodeByValue(this, element, bucketHeadNode, noNode, headSetter, tailSetter) != noNode;

            if (removed) {

                decrementNumElements();
            }
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

        final long noNode = NO_LONG_NODE;

        final boolean newAdded = bucketHeadNode == noNode || !buckets.contains(value, bucketHeadNode);

        if (newAdded) {

            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            buckets.addHead(this, value, bucketHeadNode, noNode, headSetter, tailSetter);
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("value", value).hex("keyMask", keyMask));
        }

        return newAdded;
    }

    private static void clearHashArray(long[] bucketHeadNodesHashArray) {

        LongBuckets.clearHashArray(bucketHeadNodesHashArray);
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
