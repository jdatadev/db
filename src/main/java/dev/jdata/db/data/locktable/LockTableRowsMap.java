package dev.jdata.db.data.locktable;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.lists.IntMultiList;
import dev.jdata.db.utils.adt.lists.LongMultiList;
import dev.jdata.db.utils.adt.maps.BaseLongMap;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

final class LockTableRowsMap extends BaseLongMap<LockTableRowsMap.RowsMapValues> implements ILockTableRowsMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_LOCK_TABLE_ROWS;

    static final class RowsMapValues {

        private final long[] locks;
        private final long[] transactionIdListsHeadNodes;
        private final long[] transactionIdListsTailNodes;
        private final long[] statementIdListsHeadNodes;
        private final long[] statementIdListsTailNodes;

        private RowsMapValues(int capacity) {

            this.locks = new long[capacity];
            this.transactionIdListsHeadNodes = new long[capacity];
            this.transactionIdListsTailNodes = new long[capacity];
            this.statementIdListsHeadNodes = new long[capacity];
            this.statementIdListsTailNodes = new long[capacity];
        }

        void put(int index, long lock, long transactionIdListsHeadNode, long transactionIdListsTailNode, long statementIdListsHeadNode, long statementIdListsTailNode) {

            locks[index] = lock;
            transactionIdListsHeadNodes[index] = transactionIdListsHeadNode;
            transactionIdListsTailNodes[index] = transactionIdListsTailNode;
            statementIdListsHeadNodes[index] = statementIdListsHeadNode;
            statementIdListsTailNodes[index] = statementIdListsTailNode;
        }

        void put(RowsMapValues values, int index, int newIndex) {

            locks[newIndex] = values.locks[index];
            transactionIdListsHeadNodes[newIndex] = values.transactionIdListsHeadNodes[index];
            transactionIdListsTailNodes[newIndex] = values.transactionIdListsTailNodes[index];
            statementIdListsHeadNodes[newIndex] = values.statementIdListsHeadNodes[index];
            statementIdListsTailNodes[newIndex] = values.statementIdListsTailNodes[index];
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
    public long getTransactionIdListsHeadNode(long index) {

        return getValues().transactionIdListsHeadNodes[intIndex(index)];
    }

    @Override
    public long getTransactionIdListsTailNode(long index) {

        return getValues().transactionIdListsTailNodes[intIndex(index)];
    }

    @Override
    public long getStatementIdListsHeadNode(long index) {

        return getValues().statementIdListsHeadNodes[intIndex(index)];
    }

    @Override
    public long getStatementIdListsTailNode(long index) {

        return getValues().statementIdListsTailNodes[intIndex(index)];
    }

    @Override
    public void put(long key, long lock, long transactionIdListsHeadNode, long transactionIdListsTailNode, long statementIdListsHeadNode, long statementIdListsTailNode) {

        Checks.isNotNegative(key);
        Checks.isNotNegative(transactionIdListsHeadNode);
        Checks.isNotNegative(transactionIdListsTailNode);
        Checks.isNotNegative(statementIdListsHeadNode);
        Checks.isNotNegative(statementIdListsTailNode);

        if (DEBUG) {

            enter(b -> b.binary("key", key).binary("lock", lock).add("transactionIdListsHeadNode", transactionIdListsHeadNode)
                    .add("transactionIdListsTailNode", transactionIdListsTailNode).add("statementIdListsHeadNode", statementIdListsHeadNode)
                    .add("statementIdListsTailNode", statementIdListsTailNode));
        }

        final long putResult = put(key);

        final int index = getPutIndex(putResult);

        if (index != NO_INDEX) {

            getValues().put(index, lock, transactionIdListsHeadNode, transactionIdListsTailNode, statementIdListsHeadNode, statementIdListsTailNode);
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
    public LockHolders getLockHolders(long key, LongMultiList transactionIdLists, IntMultiList statementIdLists) {

        Objects.requireNonNull(transactionIdLists);
        Objects.requireNonNull(statementIdLists);

        final LockHolders result;

        if (containsKey(key)) {

            final int lockIndex = getIndex(key);

            final RowsMapValues values = getValues();

            final long transactionIdListsHeadNode = values.transactionIdListsHeadNodes[lockIndex];
            final long statementIdListsHeadNode = values.statementIdListsHeadNodes[lockIndex];

            result = new LockHolders(transactionIdLists.toArray(transactionIdListsHeadNode), statementIdLists.toArray(statementIdListsHeadNode));
        }
        else {
            result = null;
        }

        return result;
    }
}
