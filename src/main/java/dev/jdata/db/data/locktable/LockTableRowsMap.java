package dev.jdata.db.data.locktable;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.maps.BaseLongMap;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

final class LockTableRowsMap extends BaseLongMap<LockTableRowsMap.RowsMapValues> implements ILockTableRowsMap {

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

    LockTableRowsMap(int initialCapacityExponent) {
        super(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR, RowsMapValues::new);
    }

    @Override
    public long getLockIndex(long key) {

        Checks.isNotNegative(key);

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

        Checks.isNotNegative(key);
        Checks.isNotNegative(lockInfoListsHeadNode);
        Checks.isNotNegative(lockInfoListsTailNode);

        if (DEBUG) {

            enter(b -> b.binary("key", key).binary("lock", lock).add("lockInfoListsHeadNode", lockInfoListsHeadNode).add("lockInfoListsTailNode", lockInfoListsTailNode));
        }

        final long putResult = put(key);

        final int index = getPutIndex(putResult);

        if (index != NO_INDEX) {

            getValues().put(index, lock, lockInfoListsHeadNode, lockInfoListsTailNode/*, statementIdListsHeadNode, statementIdListsTailNode*/);
        }

        if (DEBUG) {

            exit(b -> b.binary("key", key));
        }
    }

    @Override
    protected void put(RowsMapValues values, int index, RowsMapValues newValues, int newIndex) {

        newValues.put(values, index, newIndex);
    }

    @Override
    protected void clearValues(RowsMapValues values) {

    }

    @Override
    public LockedRows getLockedRows() {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final long[] keysDst = new long[numElements];
        final long[] valuesDst = new long[numElements];

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src.locks[srcIndex]);

        return new IntLockedRows(keysDst, valuesDst);
    }

    @Override
    public long getLockHoldersHeadNode(long key) {

        final long result;

        if (containsKey(key)) {

            final int lockIndex = getIndex(key);

            final RowsMapValues values = getValues();

            result = values.lockInfoListsHeadNodes[lockIndex];
        }
        else {
            result = BaseList.NO_NODE;
        }

        return result;
    }
}
