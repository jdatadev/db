package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.elements.ILongIterableElements;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.LongBuckets;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLargeLongBucketSet extends BaseLargeIntegerBucketSet<LargeLongArray> implements ILongSetGetters, ILongIterableElements {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_LONG_BUCKET_SET;

    private static final Class<?> debugClass = BaseLargeLongBucketSet.class;

    private static final ILongNodeSetter<BaseLargeLongBucketSet> headSetter = DEBUG

            ? (i, h) -> {

                if (DEBUG) {

                    PrintDebug.formatln(debugClass, "set bucketHeadNode=0x%016x scratchHashArrayIndex=%d", h, i.scratchHashArrayIndex);
                }

                i.scratchHashArray.set(i.scratchHashArrayIndex, h);
            }
            : (i, h) -> i.scratchHashArray.set(i.scratchHashArrayIndex, h);

    private static final ILongNodeSetter<BaseLargeLongBucketSet> tailSetter = (i, t) -> { };

    private LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> buckets;

    private long scratchHashArrayIndex;
    private LargeLongArray scratchHashArray;

    BaseLargeLongBucketSet(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, (o, i) -> new LargeLongArray(o, i, NO_LONG_NODE),
                BaseLargeLongBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        this.buckets = createBuckets(initialOuterCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            exit();
        }
    }

    BaseLargeLongBucketSet(int initialOuterCapacityExponent, int innerCapacityExponent, long[] values) {
        this(initialOuterCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("innerCapacityExponent", innerCapacityExponent).add("values", values));
        }

        for (long value : values) {

            addValue(value);
        }

        if (DEBUG) {

            exit();
        }
    }

    private BaseLargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final LargeLongArray bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNodesLength = bucketHeadNodesHashArray.getLimit();

        final LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> b = buckets;

        final long noNode = NO_LONG_NODE;

        for (long i = 0L; i < bucketHeadNodesLength; ++ i) {

            for (long node = bucketHeadNodesHashArray.get(i); node != noNode; node = b.getNextNode(node)) {

                forEach.each(b.getValue(node), parameter);
            }
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final LargeLongArray bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNodesLength = bucketHeadNodesHashArray.getLimit();

        final LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> b = buckets;

        final long noNode = NO_LONG_NODE;

        outer:
            for (long i = 0L; i < bucketHeadNodesLength; ++ i) {

                for (long node = bucketHeadNodesHashArray.get(i); node != noNode; node = b.getNextNode(node)) {

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

            enter(b -> b.add("value", value));
        }

        final long keyMask = getKeyMask();
        final long hashArrayIndex = HashFunctions.longHashArrayIndex(value, keyMask);

        final LargeLongArray bucketHeadNodeHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodeHashArray.get(hashArrayIndex);

        final boolean found = bucketHeadNode != NO_LONG_NODE && buckets.contains(value, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.hex("keyMask", keyMask).add("hashArrayIndex", hashArrayIndex).add("value", value)
                    .add("bucketHeadNodeHashArray", bucketHeadNodeHashArray.toHexString()).add("bucketHeadNode", bucketHeadNode));
        }

        return found;
    }

    @Override
    protected final LargeLongArray rehash(LargeLongArray hashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray.toHexString()).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent)
                    .hex("newKeyMask", newKeyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();

        final int newOuterCapacity = computeOuterCapacity(newCapacity, newCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            debug("computed capacity", b -> b.add("newOuterCapacity", newOuterCapacity).add("newCapacity", newCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        final LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> oldBuckets = buckets;

        final LargeLongArray newBucketHeadNodeHashArray = new LargeLongArray(newOuterCapacity, innerCapacityExponent, NO_LONG_NODE);
        final LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> newBuckets = createBuckets(newCapacityExponent, innerCapacityExponent);

        LongBuckets.clearHashArray(newBucketHeadNodeHashArray);

        final long hashArrayLength = hashArray.getLimit();

        final long noNode = NO_LONG_NODE;

        for (long i = 0L; i < hashArrayLength; ++ i) {

            final long bucketHeadNode = hashArray.get(i);

            for (long node = bucketHeadNode; node != noNode; node = oldBuckets.getNextNode(node)) {

                add(newBucketHeadNodeHashArray, newBuckets, oldBuckets.getValue(node), newKeyMask);
            }
        }

        this.buckets = newBuckets;

        if (DEBUG) {

            exit(newBucketHeadNodeHashArray, b -> b.add("hashArray", hashArray.toHexString()).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent)
                    .hex("newKeyMask", newKeyMask));
        }

        return newBucketHeadNodeHashArray;
    }

    final boolean addValue(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final long keyMask = checkCapacity(1);

        final boolean newAdded = add(getHashed(), buckets, value, keyMask);

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

        final long keyMask = getKeyMask();
        final long hashArrayIndex = HashFunctions.longHashArrayIndex(element, keyMask);

        final LargeLongArray bucketHeadNodeHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodeHashArray.get(hashArrayIndex);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d value=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashArrayIndex, element, keyMask, bucketHeadNode);
        }

        final long noNode = NO_LONG_NODE;

        if (bucketHeadNode == noNode) {

            removed = false;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = getHashed();

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

    final void clearBaseLargeLongBucketSet() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        buckets.clear();

        if (DEBUG) {

            exit();
        }
    }

    private boolean add(LargeLongArray bucketHeadNodeHashArray, LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> buckets, long value, long keyMask) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodeHashArray", bucketHeadNodeHashArray.toHexString()).add("buckets", buckets).hex("value", value).hex("keyMask", keyMask));
        }

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(value, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d value=%d keyMask=0x%08x", hashArrayIndex, value, keyMask);
        }

        final long noNode = NO_LONG_NODE;

        final long bucketHeadNode = hashArrayIndex < bucketHeadNodeHashArray.getLimit() ? bucketHeadNodeHashArray.get(hashArrayIndex) : noNode;

        final boolean newAdded = bucketHeadNode == noNode|| !buckets.contains(value, bucketHeadNode);

        if (newAdded) {

            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodeHashArray;

            buckets.addHead(this, value, bucketHeadNode, noNode, headSetter, tailSetter);
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("bucketHeadNodeHashArray", bucketHeadNodeHashArray.toHexString()).add("buckets", buckets).hex("value", value));
        }

        return newAdded;
    }

    private static void clearHashArray(LargeLongArray bucketHeadNodesHashArray) {

        LongBuckets.clearHashArray(bucketHeadNodesHashArray);
    }

    @Override
    public final String toString() {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final StringBuilder sb = new StringBuilder(numElements * 10);

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final LargeLongArray bucketHeadNodeHashArray = getHashed();

        ByIndex.closureOrConstantToString(bucketHeadNodeHashArray, 0L, numElements, sb, null, (s, i) -> s.get(i) != NO_LONG_NODE, (s, i, b) -> b.append(s.get(i)));

        sb.append(']');

        return sb.toString();
    }
}
