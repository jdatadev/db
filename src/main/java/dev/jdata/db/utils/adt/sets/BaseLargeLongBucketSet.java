package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.elements.ILongIterableElements;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLargeLongBucketSet extends BaseLargeIntegerBucketSet<LargeLongArray> implements ILongSetGetters, ILongIterableElements {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_LONG_BUCKET_SET;

    private static final Class<?> debugClass = BaseLargeLongBucketSet.class;

    private static final long NO_LONG_NODE = BaseList.NO_NODE;

//    private static final LongNodeSetter<BaseLargeLongBucketSet> headSetter = (i, h) -> i.getHashed().set(i.scratchHashArrayIndex, h);

    private static final LongNodeSetter<BaseLargeLongBucketSet> headSetter = (i, h) -> {

        if (DEBUG) {

            PrintDebug.formatln(debugClass, "set bucketHeadNode=0x%016x scratchHashArrayIndex=%d", h, i.scratchHashArrayIndex);
        }

        i.scratchHashArray.set(i.scratchHashArrayIndex, h);
    };

    private static final LongNodeSetter<BaseLargeLongBucketSet> tailSetter = (i, t) -> { };

    private LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> buckets;

    private long scratchHashArrayIndex;
    private LargeLongArray scratchHashArray;

    BaseLargeLongBucketSet() {
        this(BUCKETS_OUTER_INITIAL_CAPACITY, BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    BaseLargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, innerCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    BaseLargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor, (o, i) -> new LargeLongArray(o, i, NO_LONG_NODE), LargeHashArray::clearHashArray);

        this.buckets = createBuckets(initialOuterCapacity, innerCapacityExponent);
    }

    BaseLargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent, long[] values) {
        this(initialOuterCapacity, innerCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);

        for (long value : values) {

            addValue(value);
        }
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final LargeLongArray bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNodesLength = bucketHeadNodesHashArray.getLimit();

        final LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> b = buckets;

        final long noNode = BaseList.NO_NODE;

        for (long i = 0L; i < bucketHeadNodesLength; ++ i) {

            for (long n = bucketHeadNodesHashArray.get(i); n != noNode; n = b.getNextNode(n)) {

                forEach.each(b.getValue(n), parameter);
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

        final long noNode = BaseList.NO_NODE;

        outer:
            for (long i = 0L; i < bucketHeadNodesLength; ++ i) {

                for (long n = bucketHeadNodesHashArray.get(i); n != noNode; n = b.getNextNode(n)) {

                    final R forEachResult = forEach.each(b.getValue(n), parameter1, parameter2);

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

        final boolean found = bucketHeadNode != BaseList.NO_NODE && buckets.contains(value, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.hex("keyMask", keyMask).add("hashArrayIndex", hashArrayIndex).add("value", value)
                    .add("bucketHeadNodeHashArray", bucketHeadNodeHashArray.toHexString()).add("bucketHeadNode", bucketHeadNode));
        }

        return found;
    }

    @Override
    protected final LargeLongArray rehash(LargeLongArray hashArray, long newCapacity, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray.toHexString()).add("newCapacity", newCapacity).hex("newKeyMask", newKeyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();

        final int newOuterCapacity = computeOuterCapacity(newCapacity, innerCapacityExponent);

        if (DEBUG) {

            debug("computed capacity", b -> b.add("newOuterCapacity", newOuterCapacity).add("newCapacity", newCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        final LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> oldBuckets = buckets;

        final LargeLongArray newBucketHeadNodeHashArray = new LargeLongArray(newOuterCapacity, innerCapacityExponent, NO_LONG_NODE);
        final LargeLongMultiHeadSinglyLinkedList<BaseLargeLongBucketSet> newBuckets = createBuckets(newOuterCapacity, innerCapacityExponent);

        LargeHashArray.clearHashArray(newBucketHeadNodeHashArray);

        final long hashArrayLength = hashArray.getLimit();

        final long noLongNode = NO_LONG_NODE;
        final long noNode = BaseList.NO_NODE;

        for (long i = 0L; i < hashArrayLength; ++ i) {

            final long bucketHeadNode = hashArray.get(i);

            if (bucketHeadNode != noLongNode) {

                for (long node = bucketHeadNode; node != noNode; node = oldBuckets.getNextNode(node)) {

                    add(newBucketHeadNodeHashArray, newBuckets, oldBuckets.getValue(node), newKeyMask);
                }
            }
        }

        this.buckets = newBuckets;

        if (DEBUG) {

            exit(newBucketHeadNodeHashArray, b -> b.add("hashArray", hashArray.toHexString()).add("newCapacity", newCapacity).hex("newKeyMask", newKeyMask));
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

    final boolean removeAtMostOneValue(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final long keyMask = getKeyMask();
        final long hashArrayIndex = HashFunctions.longHashArrayIndex(value, keyMask);

        final LargeLongArray bucketHeadNodeHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodeHashArray.get(hashArrayIndex);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d value=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashArrayIndex, value, keyMask, bucketHeadNode);
        }

        final boolean removed;

        if (bucketHeadNode == BaseList.NO_NODE) {

            removed = false;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = getHashed();

            removed = buckets.removeAtMostOneNodeByValue(this, value, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter) != BaseList.NO_NODE;
        }

        if (removed) {

            decrementNumElements();
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

        final long bucketHeadNode = hashArrayIndex < bucketHeadNodeHashArray.getLimit() ? bucketHeadNodeHashArray.get(hashArrayIndex) : BaseList.NO_NODE;

        final boolean newAdded = bucketHeadNode == BaseList.NO_NODE || !buckets.contains(value, bucketHeadNode);

        if (newAdded) {

            this.scratchHashArrayIndex = hashArrayIndex;
            this.scratchHashArray = bucketHeadNodeHashArray;

            buckets.addHead(this, value, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter);
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("bucketHeadNodeHashArray", bucketHeadNodeHashArray.toHexString()).add("buckets", buckets).hex("value", value));
        }

        return newAdded;
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
