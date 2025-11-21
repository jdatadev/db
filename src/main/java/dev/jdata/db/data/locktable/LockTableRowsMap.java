package dev.jdata.db.data.locktable;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.adt.maps.InheritableLongKeyNonBucketMap;
import dev.jdata.db.utils.checks.Checks;

final class LockTableRowsMap extends InheritableLongKeyNonBucketMap<LockTableRowsMap.RowsMapValues, LockTableRowsMap> implements ILockTableRowsMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_LOCK_TABLE_ROWS;

    static final class RowsMapValues {

        private final long[] locks;
        private final long[] lockInfoListsHeadNodes;
        private final long[] lockInfoListsTailNodes;

        private RowsMapValues(int capacity) {

            this.locks = new long[capacity];
            this.lockInfoListsHeadNodes = new long[capacity];
            this.lockInfoListsTailNodes = new long[capacity];
        }

        void put(int index, long lock, long lockInfoListsHeadNode, long lockInfoListsTailNode /*, long statementIdListsHeadNode, long statementIdListsTailNode */) {

            locks[index] = lock;
            lockInfoListsHeadNodes[index] = lockInfoListsHeadNode;
            lockInfoListsTailNodes[index] = lockInfoListsTailNode;
        }

        void put(RowsMapValues values, int index, int newIndex) {

            locks[newIndex] = values.locks[index];
            lockInfoListsHeadNodes[newIndex] = values.lockInfoListsHeadNodes[index];
            lockInfoListsTailNodes[newIndex] = values.lockInfoListsTailNodes[index];
        }
    }

    LockTableRowsMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, RowsMapValues::new);
    }

    @Override
    public long getLockIndex(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.binary("key", key));
        }

        final int index = getIndex(key);

        final long result = index != NO_INDEX ? index : NO_LOCK_INDEX;

        if (DEBUG) {

            exit(result, b -> b.binary("key", key));
        }

        return result;
    }

    @Override
    public long getLock(long index) {

        return getValues().locks[intIndex(index)];
    }

    @Override
    public long getLockInfoListsHeadNode(long index) {

        return getValues().lockInfoListsHeadNodes[intIndex(index)];
    }

    @Override
    public long getLockInfoListsTailNode(long index) {

        return getValues().lockInfoListsTailNodes[intIndex(index)];
    }

    @Override
    public void put(long key, long lock, long lockInfoListsHeadNode, long lockInfoListsTailNode) {

        LongNonBucket.checkIsHashArrayElement(key);
        Checks.isNotNegative(lockInfoListsHeadNode);
        Checks.isNotNegative(lockInfoListsTailNode);

        if (DEBUG) {

            enter(b -> b.binary("key", key).binary("lock", lock).add("lockInfoListsHeadNode", lockInfoListsHeadNode).add("lockInfoListsTailNode", lockInfoListsTailNode));
        }

        final long putResult = put(key);

        final int index = IntCapacityPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            getValues().put(index, lock, lockInfoListsHeadNode, lockInfoListsTailNode/*, statementIdListsHeadNode, statementIdListsTailNode*/);
        }

        if (DEBUG) {

            exit(b -> b.binary("key", key));
        }
    }

    @Override
    public void remove(long key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        if (HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask()) == NO_INDEX) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }
    }

    @Override
    public LockedRows getLockedRows() {

        final LockedRows result;

        final long numElements = getNumElements();

        if (Array.isArrayCapacity(numElements)) {

            final int intNumElements = IOnlyElementsView.intNumElementsRenamed(numElements);

            final long[] keysDst = new long[intNumElements];
            final long[] valuesDst = new long[intNumElements];

            keysAndValues(keysDst, valuesDst, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> {

                kDst[dstIndex] = kSrc[srcIndex];
                vDst[dstIndex] = vSrc.locks[srcIndex];
            });

            result = new IntCapacityLockedRows(keysDst, valuesDst);
        }
        else {
            final IMutableLongLargeArray keysDst = IHeapMutableLongLargeArray.create(numElements);
            final IMutableLongLargeArray valuesDst = IHeapMutableLongLargeArray.create(numElements);

            keysAndValues(keysDst, valuesDst, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> {

                kDst.add(kSrc[srcIndex]);
                vDst.add(vSrc.locks[srcIndex]);
            });

            result = new ObjectLockedRows(keysDst, valuesDst);
        }

        return result;
    }

    @Override
    public long getLockHoldersHeadNode(long key) {

        final long result;

        final int lockIndex = getIndex(key);

        if (lockIndex == NO_INDEX) {

            throw new IllegalArgumentException();
        }

        final RowsMapValues values = getValues();

        result = values.lockInfoListsHeadNodes[lockIndex];

        return result;
    }

    @Override
    protected <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<long[], LockTableRowsMap, P, R> makeFromElements) {

        AllocationType.checkIsHeap(allocationType);
        Objects.requireNonNull(makeFromElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected void recreateElements() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected void resetToNull() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected void put(RowsMapValues values, int index, RowsMapValues newValues, int newIndex) {

        newValues.put(values, index, newIndex);
    }

    @Override
    protected void clearValues(RowsMapValues values) {

    }

    @Override
    protected int scanHashArrayForIndex(long key, int keyMask) {

        throw new UnsupportedOperationException();
    }
}
