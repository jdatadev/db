package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class LargeLongSet extends BaseLargeIntegerSet<LargeLongArray> implements ILongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_SET;

    private static final long NO_ELEMENT = -1L;

    private static final LongNodeSetter<LargeLongSet> headSetter = (i, h) -> i.getHashed().set(i.scratcHashSetIndex, h);
    private static final LongNodeSetter<LargeLongSet> tailSetter = (i, t) -> { };

    public static LargeLongSet of(int initialOuterCapacity, int innerCapacityExponent, long[] values) {

        return new LargeLongSet(initialOuterCapacity, innerCapacityExponent, values);
    }

    private final LargeLongMultiHeadSinglyLinkedList<LargeLongSet> buckets;

    private long scratcHashSetIndex;

    public LargeLongSet() {
        this(BUCKETS_OUTER_INITIAL_CAPACITY, BUCKETS_INNER_CAPACITY_EXPONENT, 3);
    }

    public LargeLongSet(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, innerCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public LargeLongSet(int initialOuterCapacity, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor, LargeLongArray::new, LargeLongSet::clearSet);

        this.buckets = new LargeLongMultiHeadSinglyLinkedList<>(initialOuterCapacity, CapacityExponents.computeCapacity(innerCapacityExponent));
    }

    private LargeLongSet(int initialOuterCapacity, int innerCapacityExponent, long[] values) {
        this(initialOuterCapacity, innerCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);

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

        final long hashSetIndex = HashFunctions.longHashIndex(element, getKeyMask());

        final LargeLongArray set = getHashed();

        final long bucketHeadNode = set.get(hashSetIndex);

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

        final long hashSetIndex = HashFunctions.longHashIndex(element, getKeyMask());

        final long bucketHeadNode = getHashed().get(hashSetIndex);

        if (DEBUG) {

            debugFormatln("lookup hashSetIndex=%d element=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashSetIndex, element, getKeyMask(), bucketHeadNode);
        }

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
    protected LargeLongArray rehash(LargeLongArray set, long newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("newCapacity", newCapacity));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();

        final int newOuterCapacity = Integers.checkUnsignedLongToUnsignedInt(newCapacity / CapacityExponents.computeCapacity(innerCapacityExponent));

        final LargeLongArray newSet = new LargeLongArray(newOuterCapacity, innerCapacityExponent);

        clearSet(newSet);

        final long setLength = set.getCapacity();

        for (long i = 0; i < setLength; ++ i) {

            final long element = set.get(i);

            if (element != NO_ELEMENT) {

                add(newSet, element);
            }
        }

        if (DEBUG) {

            exit(newSet);
        }

        return newSet;
    }

    private boolean add(LargeLongArray set, long value) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("value", value));
        }

        final long hashSetIndex = HashFunctions.longHashIndex(value, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashSetIndex=%d value=%d keyMask=0x%08x", hashSetIndex, value, getKeyMask());
        }

        final long bucketHeadNode = set.get(hashSetIndex);

        final boolean newAdded = bucketHeadNode == BaseList.NO_NODE || !buckets.contains(value, bucketHeadNode);

        if (newAdded) {

            this.scratcHashSetIndex = hashSetIndex;

            buckets.addHead(this, value, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter);
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("set", set).add("value", value));
        }

        return newAdded;
    }

    private static void clearSet(LargeLongArray set) {

        set.fill(NO_ELEMENT);
    }

    @Override
    public String toString() {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final StringBuilder sb = new StringBuilder(numElements * 10);

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final LargeLongArray set = getHashed();

        ByIndex.toString(set, 0, numElements, sb, null, (s, i) -> s.get(i) != NO_ELEMENT, (s, i, b) ->b.append(s.get(i)));

        sb.append(']');

        return sb.toString();
    }
}
