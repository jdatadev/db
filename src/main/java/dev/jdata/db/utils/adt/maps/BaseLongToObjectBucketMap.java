package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.lists.BaseLargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.BaseLongValues;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongToObjectBucketMap<T, MAP extends BaseLongToObjectBucketMap<T, MAP>>

        extends BaseLongKeyBucketMap<
                BaseLongToObjectBucketMap.LongToObjectBucketMapMultiHeadSinglyLinkedList<MAP, T>,
                BaseLongToObjectBucketMap.LongToObjectValues<MAP, T>,
                MAP>
        implements ILongToObjectCommonMapGetters<T>, ILongToObjectDynamicMapGetters<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_BUCKET_MAP;

    static final class LongToObjectBucketMapMultiHeadSinglyLinkedList<INSTANCE, T>

            extends BaseLargeLongMultiHeadSinglyLinkedList<INSTANCE, LongToObjectBucketMapMultiHeadSinglyLinkedList<INSTANCE, T>, LongToObjectValues<INSTANCE, T>> {

        LongToObjectBucketMapMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity, IntFunction<T[]> createArray) {
            super(initialOuterCapacity, innerCapacity, c -> new LongToObjectValues<>(c, createArray));
        }

        T getObjectValue(long node) {

            return getValues().objectValues[getOuterIndex(node)][getInnerIndex(node)];
        }
    }

    static final class LongToObjectValues<INSTANCE, T> extends BaseLongValues<LongToObjectBucketMapMultiHeadSinglyLinkedList<INSTANCE, T>, LongToObjectValues<INSTANCE, T>> {

        private final IntFunction<T[]> createArray;

        private T[][] objectValues;

        LongToObjectValues(int initialOuterCapacity, IntFunction<T[]> createArray) {
            super(initialOuterCapacity);

            this.createArray = Objects.requireNonNull(createArray);
        }

        @Override
        protected void reallocateOuter(int newOuterLength) {

            if (newOuterLength <= objectValues.length) {

                throw new IllegalArgumentException();
            }

            super.reallocateOuter(newOuterLength);

            this.objectValues = Arrays.copyOf(objectValues, newOuterLength);
        }

        @Override
        protected void allocateInner(int outerIndex, int innerCapacity) {

            Checks.isIndex(outerIndex);
            Checks.isCapacity(innerCapacity);

            super.allocateInner(outerIndex, innerCapacity);

            objectValues[outerIndex] = createArray.apply(innerCapacity);
        }
    }

    private final IntFunction<T[]> createArray;

    BaseLongToObjectBucketMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, (o, i) -> new LongToObjectBucketMapMultiHeadSinglyLinkedList<>(o, i, createArray), c -> new LongToObjectValues<>(c, createArray));

        this.createArray = Objects.requireNonNull(createArray);
    }

    BaseLongToObjectBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, (o, i) -> new LongToObjectBucketMapMultiHeadSinglyLinkedList<>(o, i, createArray),
                c -> new LongToObjectValues<>(c, createArray));

        this.createArray = Objects.requireNonNull(createArray);
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<T, P> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noElement = BaseList.NO_NODE;
        final LongToObjectBucketMapMultiHeadSinglyLinkedList<MAP, T> buckets = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long n = bucketHeadNode; n != noElement; n = buckets.getNextNode(n)) {

                forEach.each(buckets.getValue(n), buckets.getObjectValue(n), parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<T, P, DELEGATE, R> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final long[] bucketHeadNodesHashArray = getHashed();
        final int bucketHeadNodesHashArrayLength = bucketHeadNodesHashArray.length;

        final long noElement = BaseList.NO_NODE;
        final LongToObjectBucketMapMultiHeadSinglyLinkedList<MAP, T> buckets = getBuckets();

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long n = bucketHeadNode; n != noElement; n = buckets.getNextNode(n)) {

                final R forEachResult = forEach.each(buckets.getValue(n), buckets.getObjectValue(n), parameter, delegate);

                if (forEachResult != null) {

                    result = forEachResult;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        return result;
    }

    @Override
    public final void keysAndValues(long[] keysDst, T[] valuesDst) {

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
        final LongToObjectBucketMapMultiHeadSinglyLinkedList<MAP, T> buckets = getBuckets();

        int dstIndex = 0;

        for (int i = 0; i < bucketHeadNodesHashArrayLength; ++ i) {

            final long bucketHeadNode = bucketHeadNodesHashArray[i];

            for (long n = bucketHeadNode; n != noElement; n = buckets.getNextNode(n)) {

                keysDst[dstIndex] = buckets.getValue(n);
                valuesDst[dstIndex] = buckets.getObjectValue(n);

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }
    }

    @Override
    public final T get(long key, T defaultValue) {

        final long node = getValueNode(key);

        return node != BaseList.NO_NODE ? getBuckets().getObjectValue(node) : null;
    }

    @Override
    final LongToObjectBucketMapMultiHeadSinglyLinkedList<MAP, T> createBuckets(int outerInitialCapacity, int bucketsInnerCapacity) {

        return new LongToObjectBucketMapMultiHeadSinglyLinkedList<>(outerInitialCapacity, bucketsInnerCapacity, createArray);
    }

    final void clearBaseLongToObjectBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
