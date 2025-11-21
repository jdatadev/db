package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.adt.elements.ILongUnorderedAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.LongBuckets;
import dev.jdata.db.utils.adt.lists.IHeapMutableLongLargeSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.IMutableLongLargeSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongBucketSet

        extends BaseBucketSet<long[], IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongBucketSet>, BaseLongBucketSet>
        implements IBaseLongSetCommon, ILongUnorderedAddable {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_BUCKET_SET;

    private static final ILongNodeSetter<BaseLongBucketSet> headSetter = (i, h) -> i.scratchHashArray[i.scratchHashArrayIndex] = h;
    private static final ILongNodeSetter<BaseLongBucketSet> tailSetter = (i, t) -> { };

    private int scratchHashArrayIndex;
    private long[] scratchHashArray;

    BaseLongBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, bucketsInnerCapacityExponent, long[]::new, BaseLongBucketSet::clearHashArray,
                IHeapMutableLongLargeSinglyLinkedMultiHeadNodeList::create);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent));
        }

        Checks.isIntCapacityExponent(bucketsInnerCapacityExponent);

        if (DEBUG) {

            exit();
        }
    }

    BaseLongBucketSet(AllocationType allocationType, long[] values) {
        this(allocationType, computeRehashCapacityExponent(values.length, DEFAULT_LOAD_FACTOR), DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("values", values));
        }

        for (long value : values) {

            addValue(value);
        }

        if (DEBUG) {

            exit();
        }
    }

    private BaseLongBucketSet(AllocationType allocationType, int initialCapacityExponent, float loadFactor) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongBucketSet> buckets = getBuckets();

        final long noNode = NO_LONG_NODE;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            for (long node = bucketHeadNodesHashArray[i]; node != noNode; node = buckets.getNextNode(node)) {

                forEach.each(buckets.getValue(node), parameter);
            }
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final long[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongBucketSet> buckets = getBuckets();

        final long noNode = NO_LONG_NODE;

        outer:
            for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

                for (long node = bucketHeadNodesHashArray[i]; node != noNode; node = buckets.getNextNode(node)) {

                    final R forEachResult = forEach.each(buckets.getValue(node), parameter1, parameter2);

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

        final boolean found = bucketHeadNode != NO_LONG_NODE && getBuckets().contains(value, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("element", value));
        }

        return found;
    }

    @Override
    public final void addUnordered(long value) {

        addValue(value);
    }

    @Override
    public final void addUnordered(long[] values, int startIndex, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkIntAddFromArray(values, startIndex, numElements);

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addValue(values[i]);
        }
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<long[], BaseLongBucketSet, P, R> makeFromElements) {

        checkMakeFromElementsParameters(allocationType, parameter, makeFromElements);

        return makeFromElements.apply(allocationType, long[]::new, this, getMakeFromElementsNumElements(), parameter);
    }

    @Override
    protected final void recreateElements() {

        replaceAndClearHashed(new long[getHashedCapacity()]);
    }

    @Override
    protected final void resetToNull() {

        super.resetToNull();

        this.scratchHashArray = null;
    }

    @Override
    protected final long[] copyValues(long[] values, long startIndex, long numElements) {

        checkIntCopyValuesParameters(values, values.length, startIndex, numElements);

        return Arrays.copyOfRange(values, intIndex(startIndex), intIndex(startIndex + numElements));
    }

    @Override
    protected final void initializeWithValues(long[] values, long numElements) {

        if (DEBUG) {

            enter(b -> b.add("values", values).add("numElements", numElements));
        }

        checkIntIntitializeWithValuesParameters(values, 0L, numElements);

        addUnordered(values, 0, intNumElements(numElements));

        if (DEBUG) {

            exit();
        }
    }

    @Override
    final void rehashBuckets(long[] hashArray, long[] newHashArray, int newKeyMask, IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongBucketSet> buckets,
            IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongBucketSet> newBuckets) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("buckets", buckets).add("newBuckets", newBuckets));
        }

        final int hashArrayLength = hashArray.length;

        final long noNode = NO_LONG_NODE;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long bucketHeadNode = hashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                add(newHashArray, buckets.getValue(node), newKeyMask);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("buckets", buckets).add("newBuckets", newBuckets));
        }
    }

    final boolean addValue(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacityForOneMoreElement();

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

            removed = getBuckets().removeAtMostOneNodeByValue(this, element, bucketHeadNode, noNode, headSetter, tailSetter) != noNode;

            if (removed) {

                decrementNumElements();
            }
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
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

        final IMutableLongLargeSinglyLinkedMultiHeadNodeList<BaseLongBucketSet> buckets = getBuckets();

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

        final StringBuilder sb = new StringBuilder(IOnlyElementsView.intNumElements(this) * 10);

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final long[] bucketHeadNodesHashArray = getHashed();

        Array.toString(bucketHeadNodesHashArray, 0, bucketHeadNodesHashArray.length, sb, e -> e != NO_LONG_NODE);

        sb.append(']');

        return sb.toString();
    }
}
