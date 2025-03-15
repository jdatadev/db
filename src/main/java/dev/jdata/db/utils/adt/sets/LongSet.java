package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class LongSet extends BaseIntegerSet<long[]> implements ILongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_SET;

    private static final boolean ASSERT = AssertionContants.ASSERT_LONG_SET;

    private static final long NO_ELEMENT = -1L;

    private static final LongNodeSetter<LongSet> headSetter = (i, h) -> i.scratchSet[i.scratcHashSetIndex] = h;
    private static final LongNodeSetter<LongSet> tailSetter = (i, t) -> { };

    public static LongSet of(long ... values) {

        return new LongSet(values);
    }

    private final int bucketsInnerCapacity;

    private LargeLongMultiHeadSinglyLinkedList<LongSet> buckets;

    private int scratcHashSetIndex;
    private long[] scratchSet;

    public LongSet() {
        this(3);
    }

    public LongSet(int initialCapacityExponent) {
        this(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public LongSet(int initialCapacityExponent, float loadFactor) {
        this(initialCapacityExponent, loadFactor, BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    private LongSet(int initialCapacityExponent, float loadFactor, int bucketsInnerCapacityExponent) {
        super(initialCapacityExponent, loadFactor, long[]::new, LongSet::clearSet);

        Checks.isCapacityExponent(bucketsInnerCapacityExponent);

        final int bucketsInnerCapacity = CapacityExponents.computeCapacity(bucketsInnerCapacityExponent);

        this.bucketsInnerCapacity = bucketsInnerCapacity;

        this.buckets = new LargeLongMultiHeadSinglyLinkedList<>(BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);
    }

    private LongSet(long[] values) {
        this(computeCapacityExponent(values.length, HashedConstants.DEFAULT_LOAD_FACTOR), HashedConstants.DEFAULT_LOAD_FACTOR);

        for (long value : values) {

            add(value);
        }
    }

    @Override
    public void clear() {

        super.clear();

        buckets.clear();
    }

    @Override
    public boolean contains(long element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int hashSetIndex = HashFunctions.hashIndex(element, getKeyMask());

        final long[] set = getHashed();

        final long bucketHeadNode = set[hashSetIndex];

        final boolean found = bucketHeadNode != BaseList.NO_NODE && buckets.contains(element, bucketHeadNode);

        if (DEBUG) {

            exit(found, b -> b.add("element", element));
        }

        return found;
    }

    @Override
    public void add(long value) {

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

    @Override
    public boolean remove(long element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int hashSetIndex = HashFunctions.hashIndex(element, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%d keyMask=0x%08x", hashSetIndex, element, getKeyMask());
        }

        final long bucketHeadNode = getHashed()[hashSetIndex];

        final boolean removed;

        if (bucketHeadNode == BaseList.NO_NODE) {

            removed = false;
        }
        else {
            this.scratcHashSetIndex = hashSetIndex;

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
    protected long[] rehash(long[] set, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("newCapacity", newCapacity));
        }

        final long[] newSet = new long[newCapacity];

        clearSet(newSet);

        final LargeLongMultiHeadSinglyLinkedList<LongSet> oldBuckets = buckets;

        final int newBucketsOuterCapacity = oldBuckets.getNumOuterAllocatedEntries();

        final LargeLongMultiHeadSinglyLinkedList<LongSet> newBuckets = new LargeLongMultiHeadSinglyLinkedList<>(newBucketsOuterCapacity, bucketsInnerCapacity);

        this.buckets = newBuckets;

        final int setLength = set.length;

        for (int i = 0; i < setLength; ++ i) {

            final long element = set[i];

            if (element != NO_ELEMENT) {

                for (long node = element; node != BaseList.NO_NODE; node = oldBuckets.getNextNode(node)) {

                    add(newSet, oldBuckets.getValue(node));
                }
            }
        }

        if (DEBUG) {

            exit(newSet);
        }

        return newSet;
    }

    private boolean add(long[] set, long value) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("value", value));
        }

        final int hashSetIndex = HashFunctions.hashIndex(value, getKeyMask());

        final long bucketHeadNode = set[hashSetIndex];

        if (DEBUG) {

            debugFormatln("lookup hashSetIndex=%d value=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashSetIndex, value, getKeyMask(), bucketHeadNode);
        }

        final boolean newAdded = bucketHeadNode == BaseList.NO_NODE || !buckets.contains(value, bucketHeadNode);

        if (newAdded) {

            this.scratcHashSetIndex = hashSetIndex;
            this.scratchSet = set;

            buckets.addHead(this, value, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter);
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("set", set).add("value", value));
        }

        return newAdded;
    }

    private static void clearSet(long[] set) {

        Arrays.fill(set, NO_ELEMENT);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final long[] set = getHashed();

        Array.toString(set, 0, set.length, sb, e -> e != NO_ELEMENT);

        sb.append(']');

        return sb.toString();
    }
}
