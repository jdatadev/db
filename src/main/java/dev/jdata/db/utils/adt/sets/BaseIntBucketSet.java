package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.IntBuckets;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.LargeIntMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.strings.StringBuilders;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntBucketSet extends BaseIntegerBucketSet<int[]> implements IIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_BUCKET_SET;

    private static final Class<?> debugClass = BaseIntBucketSet.class;

    private static final ILongNodeSetter<BaseIntBucketSet> headSetter = DEBUG

            ? (i, h) -> {

                final int integer = IntBuckets.nodeToInt(h);

                i.scratchHashArray[i.scratchHashArrayIndex] = integer;

                if (DEBUG) {

                    printNode(h, i, integer);
                }
            }
            : (i, h) -> i.scratchHashArray[i.scratchHashArrayIndex] = IntBuckets.nodeToInt(h);

    private static final ILongNodeSetter<BaseIntBucketSet> tailSetter = (i, t) -> { };

    private static void printNode(long bucketHeadNode, BaseIntBucketSet intBucketSet, int integer) {

        final StringBuilder sb = new StringBuilder();

        Array.toString(intBucketSet.scratchHashArray, 0, intBucketSet.scratchHashArray.length, sb, i -> true, (a, i, b) -> StringBuilders.hexString(b, a[i], true));

        PrintDebug.formatln(debugClass, "set bucketHeadnode=0x%016x integer=0x%08x scratchHashArrayIndex=%d %s", bucketHeadNode, integer, intBucketSet.scratchHashArrayIndex,
                sb.toString());
    }

    private LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> buckets;

    private int scratchHashArrayIndex;
    private int[] scratchHashArray;

    BaseIntBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new, BaseIntBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent));
        }

        Checks.isIntCapacityExponent(bucketsInnerCapacityExponent);

        final int bucketsInnerCapacity = CapacityExponents.computeIntCapacityFromExponent(bucketsInnerCapacityExponent);

        this.buckets = new LargeIntMultiHeadSinglyLinkedList<>(DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);

        if (DEBUG) {

            exit();
        }
    }

    BaseIntBucketSet(int[] values) {
        this(computeRehashCapacityExponent(values.length, DEFAULT_LOAD_FACTOR));

        if (DEBUG) {

            enter(b -> b.add("values", values));
        }

        for (int value : values) {

            addValue(value);
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntBucketSet(BaseIntBucketSet toCopy) {
        super(toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy));
        }

        final LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> toCopyBuckets = toCopy.buckets;

        this.buckets = new LargeIntMultiHeadSinglyLinkedList<>(toCopyBuckets.getOuterArrayCapacity(), toCopyBuckets.getInnerArrayCapacity());

        forEach(this, (e, p) -> p.addValue(e));

        if (DEBUG) {

            exit();
        }
    }

    private BaseIntBucketSet(int initialCapacityExponent) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final int[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> b = buckets;

        final int noIntNode = IntBuckets.NO_INT_NODE;
        final long noNode = NO_LONG_NODE;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final int bucketHeadIntNode = bucketHeadNodesHashArray[i];

            if (bucketHeadIntNode != noIntNode) {

                final long bucketHeadNode = IntBuckets.intToNode(bucketHeadIntNode);

                for (long node = bucketHeadNode; node != noNode; node = b.getNextNode(node)) {

                    forEach.each(b.getValue(node), parameter);
                }
            }
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final int[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> b = buckets;

        final int noIntNode = IntBuckets.NO_INT_NODE;
        final long noNode = NO_LONG_NODE;

        outer:
            for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

                final int bucketHeadIntNode = bucketHeadNodesHashArray[i];

                if (bucketHeadIntNode != noIntNode) {

                    final long bucketHeadNode = IntBuckets.intToNode(bucketHeadIntNode);

                    for (long node = bucketHeadNode; node != noNode; node = b.getNextNode(node)) {

                        final R forEachResult = forEach.each(b.getValue(node), parameter1, parameter2);

                        if (forEachResult != null) {

                            result = forEachResult;

                            break outer;
                        }
                    }
                }
            }

        return result;
    }

    @Override
    public final boolean contains(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, getKeyMask());

        final int[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = IntBuckets.intToNode(bucketHeadNodesHashArray[hashArrayIndex]);

        final boolean found = bucketHeadNode != NO_LONG_NODE && buckets.contains(value, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("value", value));
        }

        return found;
    }

    @Override
    public final IHeapIntSet toHeapAllocated() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final int[] rehash(int[] hashArray, int newCapacity, int newCapacityExponent, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("keyMask", keyMask));
        }

        final int[] newBucketHeadNodesHashArray = new int[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> oldBuckets = buckets;

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LargeIntMultiHeadSinglyLinkedList<BaseIntBucketSet> newBuckets = new LargeIntMultiHeadSinglyLinkedList<>(newBucketsOuterCapacity, buckets.getInnerArrayCapacity());

        this.buckets = newBuckets;

        final int noIntNode = IntBuckets.NO_INT_NODE;
        final long noNode = NO_LONG_NODE;

        final int setLength = hashArray.length;

        for (int i = 0; i < setLength; ++ i) {

            final int bucketHeadIntNode = hashArray[i];

            if (bucketHeadIntNode != noIntNode) {

                final long bucketHeadNode = IntBuckets.intToNode(bucketHeadIntNode);

                for (long node = bucketHeadNode; node != noNode; node = oldBuckets.getNextNode(node)) {

                    add(newBucketHeadNodesHashArray, oldBuckets.getValue(node), keyMask);
                }
            }
        }

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent)
                    .hex("keyMask", keyMask));
        }

        return newBucketHeadNodesHashArray;
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

        final long bucketHeadNode = IntBuckets.intToNode(bucketHeadNodesHashArray[hashArrayIndex]);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d value=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashArrayIndex, value, keyMask, bucketHeadNode);
        }

        final long noNode = NO_LONG_NODE;

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

    final boolean removeElement(int element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int keyMask = getKeyMask();
        final int hashArrayIndex = HashFunctions.hashArrayIndex(element, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d value=%d keyMask=0x%08x", hashArrayIndex, element, keyMask);
        }

        final int[] bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNode = IntBuckets.intToNode(bucketHeadNodesHashArray[hashArrayIndex]);

        final long noNode = NO_LONG_NODE;

        final boolean removed;

        if (bucketHeadNode == noNode) {

            removed = false;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodesHashArray;

            removed = buckets.removeNodeByValue(this, element, bucketHeadNode, noNode, headSetter, tailSetter) != noNode;

            if (removed) {

                decrementNumElements();
            }
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
    }

    final void clearBaseIntBucketSet() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        buckets.clear();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final int[] bucketHeadNodesHashArray = getHashed();

        Array.toString(bucketHeadNodesHashArray, 0, bucketHeadNodesHashArray.length, sb, e -> e != IntBuckets.NO_INT_NODE);

        sb.append(']');

        return sb.toString();
    }

    private static void clearHashArray(int[] bucketHeadNodesHashArray) {

        IntBuckets.clearHashArray(bucketHeadNodesHashArray);
    }
}
