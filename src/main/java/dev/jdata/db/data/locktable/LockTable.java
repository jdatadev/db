package dev.jdata.db.data.locktable;

import dev.jdata.db.data.BaseRows;
import dev.jdata.db.utils.adt.lists.IntMultiHeadDoublyLinkedList;
import dev.jdata.db.utils.adt.lists.LongMultiHeadDoublyLinkedList;
import dev.jdata.db.utils.adt.maps.LongToLongMap;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;

public final class LockTable extends BaseRows {

    private final LongToLongMap locksByRow;

    private final LongToLongMap transactionIdListHeadIndexByRow;
    private final LongToLongMap statementIdListHeadIndexByRow;

/*
    private final LongLargeArray transactionIds;
    private IntLargeArray statementIds;
*/

    private final LongMultiHeadDoublyLinkedList<LockTable> transactionIdsList;
    private final IntMultiHeadDoublyLinkedList<LockTable> statementIdsList;

//    private IntLargeArray statementIds;

    private long scratchLong;

    private static final int READ_NUM_BITS = 32;
    private static final int READ_SHIFT = 32;
    private static final long READ_MASK = BitsUtil.maskLong(READ_NUM_BITS, READ_SHIFT);

    private static final int WRITE_NUM_BITS = 32;
    private static final int WRITE_SHIFT = 32;
    private static final long WRITE_MASK = BitsUtil.maskLong(WRITE_NUM_BITS, WRITE_SHIFT);

    private static final int TRANSACTION_ID_HEAD_NODE_NUM_BITS = 32;
    private static final int TRANSACTION_ID_HEAD_NODE_SHIFT = 32;
    private static final long TRANSACTION_ID_HEAD_NODE_MASK = BitsUtil.maskLong(TRANSACTION_ID_HEAD_NODE_NUM_BITS, TRANSACTION_ID_HEAD_NODE_SHIFT);

    private static final int TRANSACTION_ID_TAIL_NODE_NUM_BITS = 32;
    private static final int TRANSACTION_ID_TAIL_NODE_SHIFT = 0;
    private static final long TRANSACTION_ID_TAIL_NODE_MASK = BitsUtil.maskLong(TRANSACTION_ID_TAIL_NODE_NUM_BITS, TRANSACTION_ID_TAIL_NODE_SHIFT);

    private static final int STATEMENT_ID_HEAD_NODE_NUM_BITS = 32;
    private static final int STATEMENT_ID_HEAD_NODE_SHIFT = 32;
    private static final long STATEMENT_ID_HEAD_NODE_MASK = BitsUtil.maskLong(STATEMENT_ID_HEAD_NODE_NUM_BITS, STATEMENT_ID_HEAD_NODE_SHIFT);

    private static final int STATEMENT_ID_TAIL_NODE_NUM_BITS = 32;
    private static final int STATEMENT_ID_TAIL_NODE_SHIFT = 0;
    private static final long STATEMENT_ID_TAIL_NODE_MASK = BitsUtil.maskLong(STATEMENT_ID_TAIL_NODE_NUM_BITS, STATEMENT_ID_TAIL_NODE_SHIFT);

    public LockTable() {

        final int initialCapacityExponent = 0;

        this.locksByRow = new LongToLongMap(initialCapacityExponent);

        this.transactionIdListHeadIndexByRow = new LongToLongMap(initialCapacityExponent);
        this.statementIdListHeadIndexByRow = new LongToLongMap(initialCapacityExponent);

        final int initialOuterCapacity = 1;
        final int innerCapacity = 10000;
/*
        this.transactionIds = new LongLargeArray(initialOuterCapacity, innerCapacity);
        this.statementIds = new IntLargeArray(initialOuterCapacity, innerCapacity);
*/
        this.transactionIdsList = new LongMultiHeadDoublyLinkedList<>(initialOuterCapacity, innerCapacity);
        this.statementIdsList = new IntMultiHeadDoublyLinkedList<>(initialOuterCapacity, innerCapacity);
    }

    public synchronized void readLock(long transactionId, int statementId, int tableId, long rowId) {

        lock(transactionId, statementId, tableId, rowId, READ_MASK, READ_SHIFT);
    }

    public synchronized void readUnlock(long transactionId, int statementId, int tableId, long rowId) {

        unlock(transactionId, statementId, tableId, rowId, READ_MASK, READ_SHIFT);
    }

    public synchronized void writeLock(long transactionId, int statementId, int tableId, long rowId) {

        lock(transactionId, statementId, tableId, rowId, WRITE_MASK, WRITE_SHIFT);
    }

    public synchronized void writeUnlock(long transactionId, int statementId, int tableId, long rowId) {

        unlock(transactionId, statementId, tableId, rowId, WRITE_MASK, WRITE_SHIFT);
    }

    private void lock(long transactionId, int statementId, int tableId, long rowId, long mask, int shift) {

        checkParameters(transactionId, statementId, tableId, rowId);

        final long key = makeKey(tableId, rowId);

        final long lockBits = locksByRow.get(key);

        final long updatedLockBits = lockBits == LongToLongMap.NO_VALUE
                ? makeOne(shift)
                : increment(lockBits, mask, shift);

        locksByRow.put(key, updatedLockBits);

        final long transactionIdIndices = transactionIdListHeadIndexByRow.get(key);

        final long transactionListHeadNode = BitsUtil.applyShiftedMask(transactionIdIndices, TRANSACTION_ID_HEAD_NODE_MASK, TRANSACTION_ID_HEAD_NODE_SHIFT);
        final long transactionListTailNode = BitsUtil.applyShiftedMask(transactionIdIndices, TRANSACTION_ID_TAIL_NODE_MASK, TRANSACTION_ID_TAIL_NODE_SHIFT);

        if (!transactionIdsList.contains(transactionId, transactionListHeadNode)) {

            transactionIdsList.addHead(transactionId, transactionListHeadNode, transactionListTailNode,
                    (o, h) -> o.scratchLong = BitsUtil.setWithShiftedMask(0L, h, TRANSACTION_ID_HEAD_NODE_MASK, TRANSACTION_ID_HEAD_NODE_SHIFT),
                    (o, t) -> o.scratchLong = BitsUtil.setWithShiftedMask(o.scratchLong, t, TRANSACTION_ID_TAIL_NODE_MASK, TRANSACTION_ID_TAIL_NODE_SHIFT));

            transactionIdListHeadIndexByRow.put(key, scratchLong);
        }

        final long statementIdIndices = transactionIdListHeadIndexByRow.get(key);

        final long statementListHeadNode = BitsUtil.applyShiftedMask(statementIdIndices, STATEMENT_ID_HEAD_NODE_MASK, STATEMENT_ID_HEAD_NODE_SHIFT);
        final long statementListTailNode = BitsUtil.applyShiftedMask(statementIdIndices, STATEMENT_ID_TAIL_NODE_MASK, STATEMENT_ID_TAIL_NODE_SHIFT);

        if (!transactionIdsList.contains(transactionId, statementListHeadNode)) {

            transactionIdsList.addHead(transactionId, statementListHeadNode, statementListTailNode,
                    (o, h) -> o.scratchLong = BitsUtil.setWithShiftedMask(0L, h, STATEMENT_ID_HEAD_NODE_MASK, STATEMENT_ID_HEAD_NODE_SHIFT),
                    (o, t) -> o.scratchLong = BitsUtil.setWithShiftedMask(o.scratchLong, t, STATEMENT_ID_TAIL_NODE_MASK, STATEMENT_ID_HEAD_NODE_SHIFT));

            transactionIdListHeadIndexByRow.put(key, scratchLong);
        }

    }

    private void unlock(long transactionId, int statementId, int tableId, long rowId, long mask, int shift) {

        checkParameters(transactionId, statementId, tableId, rowId);

        final long key = makeKey(tableId, rowId);

        final long lockBits = locksByRow.get(key);

        if (lockBits == LongToLongMap.NO_VALUE) {

            throw new IllegalStateException();
        }

        final long updatedLockBits = decrement(lockBits, mask, shift);

        locksByRow.put(key, updatedLockBits);
    }

    private static long makeOne(int shift) {

        return 1L << shift;
    }

    private static long increment(long lockBits, long mask, int shift) {

        final long numReadLocked = (lockBits & mask) >>> shift;
        final long updatedLockBits = (numReadLocked + 1) << shift;

        return (lockBits & (~mask)) | updatedLockBits;
    }

    private static long decrement(long lockBits, long mask, int shift) {

        final long numReadLocked = (lockBits & mask) >>> shift;
        final long updatedLockBits = (numReadLocked - 1) << shift;

        return (lockBits & (~mask)) | updatedLockBits;
    }

    private static void checkParameters(long transactionId, int statementId, int tableId, long rowId) {

        Checks.isTransactionId(transactionId);
        Checks.isStatementId(statementId);

        checkParameters(tableId, rowId);
    }
}

