package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.lists.BaseLargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.BaseLongValues;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongToIntBucketMap<MAP extends BaseLongToIntBucketMap<MAP>>

        extends BaseLongBucketMap<
                BaseLongToIntBucketMap.LongToIntBucketMapMultiHeadSinglyLinkedList<MAP>,
                BaseLongToIntBucketMap.LongToIntValues<MAP>,
                MAP>
        implements ILongToIntMapGetters, ILongToIntBucketMapGetters {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_INT_BUCKET_MAP;

    static final class LongToIntBucketMapMultiHeadSinglyLinkedList<INSTANCE>

            extends BaseLargeLongMultiHeadSinglyLinkedList<INSTANCE, LongToIntBucketMapMultiHeadSinglyLinkedList<INSTANCE>, LongToIntValues<INSTANCE>> {

        LongToIntBucketMapMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
            super(initialOuterCapacity, innerCapacity, LongToIntValues::new);
        }

        int getIntValue(long node) {

            return getValues().intValues[getOuterIndex(node)][getInnerIndex(node)];
        }
    }

    static final class LongToIntValues<INSTANCE> extends BaseLongValues<LongToIntBucketMapMultiHeadSinglyLinkedList<INSTANCE>, LongToIntValues<INSTANCE>> {

        private int[][] intValues;

        LongToIntValues(int initialOuterCapacity) {
            super(initialOuterCapacity);
        }

        @Override
        protected void reallocateOuter(int newOuterLength) {

            if (newOuterLength <= intValues.length) {

                throw new IllegalArgumentException();
            }

            super.reallocateOuter(newOuterLength);

            this.intValues = Arrays.copyOf(intValues, newOuterLength);
        }

        @Override
        protected void allocateInner(int outerIndex, int innerCapacity) {

            Checks.isIndex(outerIndex);
            Checks.isCapacity(innerCapacity);

            super.allocateInner(outerIndex, innerCapacity);

            intValues[outerIndex] = new int[innerCapacity];
        }
    }

    BaseLongToIntBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent, LongToIntBucketMapMultiHeadSinglyLinkedList::new, LongToIntValues::new);
    }

    BaseLongToIntBucketMap(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor, LongToIntBucketMapMultiHeadSinglyLinkedList::new, LongToIntValues::new);
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<P> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEachKeyAndValue", forEachKeyAndValue));
        }

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noElement = BaseList.NO_NODE;
        final LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> buckets = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long n = bucketHeadNode; n != noElement; n = buckets.getNextNode(n)) {

                forEachKeyAndValue.each(buckets.getValue(n), buckets.getIntValue(n), parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEachKeyAndValue", forEachKeyAndValue));
        }
    }

    @Override
    public final void keysAndValues(long[] keysDst, int[] valuesDst) {

        final long numElements = getNumElements();

        Checks.isGreaterThanOrEqualTo(keysDst.length, numElements);
        Checks.isGreaterThanOrEqualTo(valuesDst.length, numElements);
        Checks.areEqual(keysDst.length, valuesDst.length);

        if (DEBUG) {

            enter();
        }

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noElement = BaseList.NO_NODE;
        final LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> buckets = getBuckets();

        int dstIndex = 0;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long n = bucketHeadNode; n != noElement; n = buckets.getNextNode(n)) {

                keysDst[dstIndex] = buckets.getValue(n);
                valuesDst[dstIndex] = buckets.getIntValue(n);

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }
    }

    @Override
    public final int get(long key, int defaultValue) {

        final long node = getValueNode(key);

        return node != BaseList.NO_NODE ? getBuckets().getIntValue(node) : defaultValue;
    }

    @Override
    final LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> createBuckets(int outerInitialCapacity, int bucketsInnerCapacity) {

        return new LongToIntBucketMapMultiHeadSinglyLinkedList<>(outerInitialCapacity, bucketsInnerCapacity);
    }

    final void clearBaseLongToIntBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
