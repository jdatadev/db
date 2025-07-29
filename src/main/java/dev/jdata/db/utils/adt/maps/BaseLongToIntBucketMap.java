package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.lists.BaseLargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.BaseLongValues;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongToIntBucketMap<MAP extends BaseLongToIntBucketMap<MAP>>

        extends BaseLongKeyBucketMap<
                BaseLongToIntBucketMap.LongToIntBucketMapMultiHeadSinglyLinkedList<MAP>,
                BaseLongToIntBucketMap.LongToIntValues<MAP>,
                MAP>
        implements ILongToIntCommonMapGetters, ILongToIntDynamicMapGetters {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_INT_BUCKET_MAP;

    static final class LongToIntBucketMapMultiHeadSinglyLinkedList<INSTANCE>

            extends BaseLargeLongMultiHeadSinglyLinkedList<INSTANCE, LongToIntBucketMapMultiHeadSinglyLinkedList<INSTANCE>, LongToIntValues<INSTANCE>> {

        LongToIntBucketMapMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
            super(initialOuterCapacity, innerCapacity, LongToIntValues::new);
        }

        int getIntValue(long node) {

            return getValues().intValues[getOuterIndex(node)][getInnerIndex(node)];
        }

        void setIntValue(long node, int value) {

            getValues().intValues[getOuterIndex(node)][getInnerIndex(node)] = value;
        }

        int getAndSetIntValue(long node, int value) {

            final int[] innerArray = getValues().intValues[getOuterIndex(node)];
            final int innerIndex = getInnerIndex(node);

            final int result = innerArray[innerIndex];

            innerArray[innerIndex] = value;

            return result;
        }
    }

    static final class LongToIntValues<INSTANCE> extends BaseLongValues<LongToIntBucketMapMultiHeadSinglyLinkedList<INSTANCE>, LongToIntValues<INSTANCE>> {

        private int[][] intValues;

        LongToIntValues(int initialOuterCapacity) {
            super(initialOuterCapacity);

            this.intValues = new int[initialOuterCapacity][];
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
        super(initialCapacityExponent, LongToIntBucketMapMultiHeadSinglyLinkedList::new);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongToIntBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, LongToIntBucketMapMultiHeadSinglyLinkedList::new);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<P> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noNode = NO_LONG_NODE;
        final LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> buckets = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                forEach.each(buckets.getValue(node), buckets.getIntValue(node), parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<P, DELEGATE, R> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        R result = null;

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noNode = NO_LONG_NODE;
        final LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> buckets = getBuckets();

        outer:
            for (int i = 0; i < bucketHeadNodesHashArrayLength && result == null; ++ i) {

                final long bucketHeadNode = bucketHeadNodesHashArray[i];

                for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                    final R forEachResult = forEach.each(buckets.getValue(node), buckets.getIntValue(node), parameter, delegate);

                    if (forEachResult != null) {

                        result = forEachResult;

                        break outer;
                    }
                }
            }

        if (result == null) {

            result = defaultResult;
        }

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        return result;
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

        final long noNode = NO_LONG_NODE;
        final LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> buckets = getBuckets();

        int dstIndex = 0;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long node = bucketHeadNode; node != noNode; node = buckets.getNextNode(node)) {

                keysDst[dstIndex] = buckets.getValue(node);
                valuesDst[dstIndex] = buckets.getIntValue(node);

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }
    }

    @Override
    public final int get(long key, int defaultValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int result;

        final long headNode = getValueNode(key);

        final long noNode = NO_LONG_NODE;

        if (headNode != noNode) {

            final LongToIntBucketMapMultiHeadSinglyLinkedList<?> buckets = getBuckets();

            final long node = buckets.findNodeWithValue(key, headNode);

            result = node != noNode ? buckets.getIntValue(node) : defaultValue;
        }
        else {
            result = defaultValue;
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    final LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> createBuckets(int outerInitialCapacity, int bucketsInnerCapacity) {

        return new LongToIntBucketMapMultiHeadSinglyLinkedList<>(outerInitialCapacity, bucketsInnerCapacity);
    }

    @Override
    void rehashPut(LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> oldBuckets, long oldNode, LongToIntBucketMapMultiHeadSinglyLinkedList<MAP> newBuckets, long newNode) {

        if (DEBUG) {

            enter(b -> b.add("oldBuckets", oldBuckets).hex("oldNode", oldNode).add("newBuckets", newBuckets).hex("newNode", newNode));
        }

        final int value = oldBuckets.getIntValue(oldNode);

        newBuckets.setIntValue(newNode, value);

        if (DEBUG) {

            exit();
        }
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
