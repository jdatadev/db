package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.byindex.ByIndex;
import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.LongBuckets;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.IMutableLongLargeSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseLongLargeBucketSet extends BaseLargeIntegerBucketSet<IMutableLongLargeArray> implements IBaseLongSetCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_LONG_BUCKET_SET;

    private static final Class<?> debugClass = BaseLongLargeBucketSet.class;

    private static final ILongNodeSetter<BaseLongLargeBucketSet> headSetter = DEBUG

            ? (i, h) -> {

                if (DEBUG) {

                    PrintDebug.formatln(debugClass, "set bucketHeadNode=0x%016x scratchHashArrayIndex=%d", h, i.scratchHashArrayIndex);
                }

                i.scratchHashArray.set(i.scratchHashArrayIndex, h);
            }
            : (i, h) -> i.scratchHashArray.set(i.scratchHashArrayIndex, h);

    private static final ILongNodeSetter<BaseLongLargeBucketSet> tailSetter = (i, t) -> { };

    private IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongLargeBucketSet> buckets;

    private long scratchHashArrayIndex;
    private IMutableLongLargeArray scratchHashArray;

    BaseLongLargeBucketSet(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, BaseLongLargeBucketSet::createHashArray,
                BaseLongLargeBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        this.buckets = createBuckets(initialOuterCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            exit();
        }
    }

    BaseLongLargeBucketSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent, long[] values) {
        this(allocationType, initialOuterCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("innerCapacityExponent", innerCapacityExponent).add("values", values));
        }

        for (long value : values) {

            addValue(value);
        }

        if (DEBUG) {

            exit();
        }
    }

    private BaseLongLargeBucketSet(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        this(allocationType, initialOuterCapacity, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final IMutableLongLargeArray bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNodesLength = bucketHeadNodesHashArray.getLimit();

        final IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongLargeBucketSet> b = buckets;

        final long noNode = NO_LONG_NODE;

        for (long i = 0L; i < bucketHeadNodesLength; ++ i) {

            for (long node = bucketHeadNodesHashArray.get(i); node != noNode; node = b.getNextNode(node)) {

                forEach.each(b.getValue(node), parameter);
            }
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final IMutableLongLargeArray bucketHeadNodesHashArray = getHashed();

        final long bucketHeadNodesLength = bucketHeadNodesHashArray.getLimit();

        final IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongLargeBucketSet> b = buckets;

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
    public final IHeapLongSet toHeapAllocated() {

        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean contains(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final long keyMask = getKeyMask();
        final long hashArrayIndex = HashFunctions.longHashArrayIndex(value, keyMask);

        final IMutableLongLargeArray bucketHeadNodeHashArray = getHashed();

        final long bucketHeadNode = bucketHeadNodeHashArray.get(hashArrayIndex);

        final boolean found = bucketHeadNode != NO_LONG_NODE && buckets.contains(value, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.hex("keyMask", keyMask).add("hashArrayIndex", hashArrayIndex).add("value", value)
                    .add("bucketHeadNodeHashArray", bucketHeadNodeHashArray.toHexString()).add("bucketHeadNode", bucketHeadNode));
        }

        return found;
    }

    @Override
    protected final IMutableLongLargeArray rehash(IMutableLongLargeArray hashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray.toHexString()).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent)
                    .hex("newKeyMask", newKeyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();

        final int newOuterCapacity = computeOuterCapacity(newCapacity, newCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            debug("computed capacity", b -> b.add("newOuterCapacity", newOuterCapacity).add("newCapacity", newCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        final IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongLargeBucketSet> oldBuckets = buckets;

        final IMutableLongLargeArray newBucketHeadNodeHashArray = createHashArray(newOuterCapacity, innerCapacityExponent);
        final IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongLargeBucketSet> newBuckets = createBuckets(newCapacityExponent, innerCapacityExponent);

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

        final IMutableLongLargeArray bucketHeadNodeHashArray = getHashed();

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

    private boolean add(IMutableLongLargeArray bucketHeadNodeHashArray, IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongLargeBucketSet> buckets, long value, long keyMask) {

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

    private static IMutableLongLargeArray createHashArray(int initialOuterCapacity, int innerCapacityExponent) {

        return IMutableLongLargeArray.create(initialOuterCapacity, innerCapacityExponent, NO_LONG_NODE);
    }

    private static void clearHashArray(IMutableLongLargeArray bucketHeadNodesHashArray) {

        LongBuckets.clearHashArray(bucketHeadNodesHashArray);
    }

    @Override
    public final String toString() {

        final int numElements = IElementsView.intNumElements(getNumElements());

        final StringBuilder sb = new StringBuilder(numElements * 10);

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final IMutableLongLargeArray bucketHeadNodeHashArray = getHashed();

        ByIndex.closureOrConstantToString(bucketHeadNodeHashArray, 0L, numElements, sb, null, (s, i) -> s.get(i) != NO_LONG_NODE, (s, i, b) -> b.append(s.get(i)));

        sb.append(']');

        return sb.toString();
    }
}
