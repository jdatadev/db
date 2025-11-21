package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.adt.elements.IIntForEachWithResult;
import dev.jdata.db.utils.adt.elements.IIntUnorderedAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.IntBuckets;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.IMutableIntLargeSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.adt.lists.IMutableIntSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.jdk.adt.strings.StringBuilders;

abstract class BaseIntBucketSet extends BaseIntegerBucketSet<int[], BaseIntBucketSet> implements IBaseIntSetCommon, IIntUnorderedAddable {

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

    private IMutableIntLargeSinglyLinkedMultiHeadNodeList<BaseIntBucketSet> buckets;

    private int scratchHashArrayIndex;
    private int[] scratchHashArray;

    BaseIntBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new, BaseIntBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent));
        }

        Checks.isIntCapacityExponent(bucketsInnerCapacityExponent);

        final int bucketsInnerCapacity = CapacityExponents.computeIntCapacityFromExponent(bucketsInnerCapacityExponent);

        this.buckets = IMutableIntLargeSinglyLinkedMultiHeadNodeList.create(DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);

        if (DEBUG) {

            exit();
        }
    }

    BaseIntBucketSet(AllocationType allocationType, int[] values) {
        this(allocationType, computeRehashCapacityExponent(values.length, DEFAULT_LOAD_FACTOR));

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("values", values));
        }

        for (int value : values) {

            addValue(value);
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntBucketSet(AllocationType allocationType, BaseIntBucketSet toCopy) {
        super(allocationType, toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        @SuppressWarnings("unchecked")
        final IMutableIntLargeSinglyLinkedMultiHeadNodeList<BaseIntBucketSet> toCopyBuckets
                = (IMutableIntLargeSinglyLinkedMultiHeadNodeList<BaseIntBucketSet>)toCopy.buckets.createEmptyWithSameCapacity();

        this.buckets = toCopyBuckets;

        new MutableLargeIntMultiHeadSinglyLinkedList<>(toCopyBuckets.getOuterArrayCapacity(), toCopyBuckets.getInnerArrayCapacity());

        forEach(this, (e, p) -> p.addValue(e));

        if (DEBUG) {

            exit();
        }
    }

    private BaseIntBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);

    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IIntForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final int[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final IMutableIntSinglyLinkedMultiHeadNodeList<BaseIntBucketSet> b = buckets;

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
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final int[] bucketHeadNodesHashArray = getHashed();

        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final IMutableIntSinglyLinkedMultiHeadNodeList<BaseIntBucketSet> b = buckets;

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
    public final void addUnordered(int[] values, int startIndex, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkIntAddFromArray(values, startIndex, numElements);

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addValue(values[i]);
        }
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<int[], BaseIntBucketSet, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, int[]::new, this, getMakeFromElementsNumElements(), parameter);
    }

    @Override
    protected final void recreateElements() {

        replaceAndClearHashed(new int[getHashedCapacity()]);
    }

    @Override
    protected final void resetToNull() {

        super.resetToNull();

        this.buckets = null;
        this.scratchHashArray = null;
    }

    @Override
    protected final int[] copyValues(int[] values, long startIndex, long numElements) {

        checkIntCopyValuesParameters(values, values.length, startIndex, numElements);

        return Arrays.copyOfRange(values, intIndex(startIndex), intIndex(startIndex + numElements));
    }

    @Override
    protected final void initializeWithValues(int[] values, long numElements) {

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
    protected final int[] rehash(int[] hashArray, int newCapacity, int newCapacityExponent, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("keyMask", keyMask));
        }

        final int[] newBucketHeadNodesHashArray = new int[newCapacity];

        clearHashArray(newBucketHeadNodesHashArray);

        final IMutableIntSinglyLinkedMultiHeadNodeList<BaseIntBucketSet> oldBuckets = buckets;

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final IMutableIntSinglyLinkedMultiHeadNodeList<BaseIntBucketSet> newBuckets = new MutableLargeIntMultiHeadSinglyLinkedList<>(newBucketsOuterCapacity, buckets.getInnerArrayCapacity());

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

        final StringBuilder sb = new StringBuilder(IOnlyElementsView.intNumElements(this) * 10);

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
