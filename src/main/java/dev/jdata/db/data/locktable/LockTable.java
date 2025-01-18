package dev.jdata.db.data.locktable;

import dev.jdata.db.DBConstants;
import dev.jdata.db.DBException;
import dev.jdata.db.DebugConstants;
import dev.jdata.db.data.BaseRows;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeIntMultiHeadDoublyLinkedList;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadDoublyLinkedList;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class LockTable extends BaseRows implements PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_LOCK_TABLE;

    private static final Class<?> debugClass = LockTable.class;

    public static abstract class LockTableException extends DBException {

        private static final long serialVersionUID = 1L;
    }

    public static final class NotLockedException extends LockTableException {

        private static final long serialVersionUID = 1L;
    }

    private static final long NO_NODE = BaseList.NO_NODE;

    private static final int READ_NUM_BITS = 32;
    private static final int READ_SHIFT = 32;
    private static final long READ_MASK = BitsUtil.maskLong(READ_NUM_BITS, READ_SHIFT);

    private static final int WRITE_NUM_BITS = 32;
    private static final int WRITE_SHIFT = 0;
    private static final long WRITE_MASK = BitsUtil.maskLong(WRITE_NUM_BITS, WRITE_SHIFT);

    public static enum LockType {

        READ,
        WRITE;

        private static LockType ofOrdinal(int ordinal) {

            return LockType.values()[ordinal];
        }

        long mask() {

            final long result;

            switch (this) {

            case READ:

                result = READ_MASK;
                break;

            case WRITE:

                result = WRITE_MASK;
                break;

            default:
                throw new UnsupportedOperationException();
            }

            return result;
        }

        int shift() {

            final int result;

            switch (this) {

            case READ:

                result = READ_SHIFT;
                break;

            case WRITE:

                result = WRITE_SHIFT;
                break;

            default:
                throw new UnsupportedOperationException();
            }

            return result;
        }
    }

    private static final int LOCK_TYPE_NUM_BITS;
    private static final long ALL_NUM_LOCKED_BITS_MASK;

    static {

        LOCK_TYPE_NUM_BITS = BitsUtil.getNumEnumBits(LockType.class);

        if (LOCK_TYPE_NUM_BITS > 1) {

            throw new IllegalStateException();
        }

        long allNumLockedBitsMask = 0L;

        for (LockType lockType : LockType.values()) {

            allNumLockedBitsMask |= lockType.mask();
        }

        ALL_NUM_LOCKED_BITS_MASK = allNumLockedBitsMask;
    }

    private static final int TRANSACTION_VALUE_LOCK_TYPE_SHIFT = DBConstants.TRANSACTION_ID_NUM_BITS - LOCK_TYPE_NUM_BITS;
    private static final long TRANSACTION_VALUE_LOCK_TYPE_MASK = BitsUtil.maskLong(LOCK_TYPE_NUM_BITS, TRANSACTION_VALUE_LOCK_TYPE_SHIFT);
    private static final int TRANSACTION_VALUE_ID_SHIFT = 0;
    private static final long TRANSACTION_VALUE_ID_MASK = BitsUtil.maskLong(DBConstants.TRANSACTION_ID_NUM_BITS - LOCK_TYPE_NUM_BITS, TRANSACTION_VALUE_ID_SHIFT);

    private static final int STATEMENT_VALUE_LOCK_TYPE_SHIFT = DBConstants.STATEMENT_ID_NUM_BITS - LOCK_TYPE_NUM_BITS;
    private static final int STATEMENT_VALUE_LOCK_TYPE_MASK = BitsUtil.maskInt(LOCK_TYPE_NUM_BITS, STATEMENT_VALUE_LOCK_TYPE_SHIFT);
    private static final int STATEMENT_VALUE_ID_SHIFT = 0;
    private static final int STATEMENT_VALUE_ID_MASK = BitsUtil.maskInt(DBConstants.STATEMENT_ID_NUM_BITS - LOCK_TYPE_NUM_BITS, STATEMENT_VALUE_ID_SHIFT);

    private final ILockTableRowsMap rowsMap;

    private final LargeLongMultiHeadDoublyLinkedList<LockTable> transactionIdLists;
    private final LargeIntMultiHeadDoublyLinkedList<LockTable> statementIdLists;

    private long scratchHeadNode;
    private long scratchTailNode;

    public LockTable() {

        final int initialCapacityExponent = 0;

        this.rowsMap = new LockTableRowsMap(initialCapacityExponent);

        final int initialOuterCapacity = 1;
        final int innerCapacity = 10000;

        this.transactionIdLists = new LargeLongMultiHeadDoublyLinkedList<>(initialOuterCapacity, innerCapacity);
        this.statementIdLists = new LargeIntMultiHeadDoublyLinkedList<>(initialOuterCapacity, innerCapacity);
    }

    public synchronized boolean tryReadLock(long transactionId, int statementId, int tableId, long rowId) {

        return tryLock(transactionId, statementId, tableId, rowId, LockType.READ);
    }

    public synchronized void readUnlock(long transactionId, int statementId, int tableId, long rowId) throws NotLockedException {

        unlock(transactionId, statementId, tableId, rowId, LockType.READ);
    }

    public synchronized boolean tryWriteLock(long transactionId, int statementId, int tableId, long rowId) {

        return tryLock(transactionId, statementId, tableId, rowId, LockType.WRITE);
    }

    public synchronized void writeUnlock(long transactionId, int statementId, int tableId, long rowId) throws NotLockedException {

        unlock(transactionId, statementId, tableId, rowId, LockType.WRITE);
    }

    private boolean tryLock(long transactionId, int statementId, int tableId, long rowId, LockType lockType) {

        checkParameters(transactionId, statementId, tableId, rowId);

        if (DEBUG) {

            enter(b -> b.add("transactionId", transactionId).add("statementId", statementId).add("tableId", tableId).add("rowId", rowId).add("lockType", lockType));
        }

        final long key = makeKey(tableId, rowId);

        final long lockIndex = rowsMap.getLockIndex(key);

        final long lockBits;
        final boolean canLock;

        if (lockIndex == ILockTableRowsMap.NO_LOCK_INDEX) {

            lockBits = makeNone();
            canLock = true;
        }
        else {
            final long transactionIdListsHeadNode = rowsMap.getTransactionIdListsHeadNode(lockIndex);

            lockBits = rowsMap.getLock(lockIndex);

            final int numWriteLocks = numLocks(lockBits, LockType.WRITE);

            if (numWriteLocks > 0) {

                if (transactionIdListsHeadNode == NO_NODE) {

                    throw new IllegalStateException();
                }
                else {
                    canLock = transactionIdLists.containsOnly(transactionId, transactionIdListsHeadNode, (i, l) -> i == transactionId(l));
                }
            }
            else {
                switch (lockType) {

                case READ:

                    canLock = true;
                    break;

                case WRITE:

                    final int numReadLocks = numLocks(lockBits, LockType.READ);

                    if (numReadLocks > 0) {

                        if (transactionIdListsHeadNode == NO_NODE) {

                            throw new IllegalStateException();
                        }
                        else {
                            canLock = transactionIdLists.containsOnly(transactionId, transactionIdListsHeadNode, (i, l) -> i == transactionId(l));
                        }
                    }
                    else {
                        canLock = true;
                    }
                    break;

                default:
                    throw new UnsupportedOperationException();
                }
            }
        }

        if (DEBUG) {

            debug(b -> b.binary("key", key).add("canLock", canLock).binary("lockBits", lockBits).binary("lockType.mask()", lockType.mask())
                    .add("lockType.shift()", lockType.shift()));
        }

        if (canLock) {

            final long updatedLockBits;

            final long transactionIdListsHeadNode;
            final long transactionIdListsTailNode;

            final long statementIdListsHeadNode;
            final long statementIdListsTailNode;

            final boolean addTransactionId;
            final boolean addStatementId;

            final long transactionValue = transactionValue(transactionId, lockType);
            final int statementValue = statementValue(statementId, lockType);

            if (lockIndex == ILockTableRowsMap.NO_LOCK_INDEX) {

                updatedLockBits = makeOne(lockType.shift());

                transactionIdListsHeadNode = NO_NODE;
                transactionIdListsTailNode = NO_NODE;

                statementIdListsHeadNode = NO_NODE;
                statementIdListsTailNode = NO_NODE;

                addTransactionId = true;
                addStatementId = true;
            }
            else {
                updatedLockBits = increment(lockBits, lockType);

                transactionIdListsHeadNode = rowsMap.getTransactionIdListsHeadNode(lockIndex);
                transactionIdListsTailNode = rowsMap.getTransactionIdListsTailNode(lockIndex);

                statementIdListsHeadNode = rowsMap.getStatementIdListsHeadNode(lockIndex);
                statementIdListsTailNode = rowsMap.getStatementIdListsTailNode(lockIndex);

                addTransactionId = !transactionIdLists.contains(transactionValue, transactionIdListsHeadNode);
                addStatementId = !statementIdLists.contains(statementValue, statementIdListsHeadNode);
            }

            final long updatedTransactionIdListsHeadNode;
            final long updatedTransactionIdListsTailNode;

            if (DEBUG) {

                debug("add transactionId", b -> b.add("addTransactionId", addTransactionId).binary("transactionValue", transactionValue)
                        .add("transactionIdListsHeadNode", transactionIdListsHeadNode).add("transactionIdListsTailNode", transactionIdListsTailNode));
            }

            if (addTransactionId) {

                transactionIdLists.addHead(this, transactionValue, transactionIdListsHeadNode, transactionIdListsTailNode, LockTable::setScratchHeadNode,
                        LockTable::setScratchTailNode);

                updatedTransactionIdListsHeadNode = scratchHeadNode;
                updatedTransactionIdListsTailNode = scratchTailNode;
            }
            else {
                updatedTransactionIdListsHeadNode = transactionIdListsHeadNode;
                updatedTransactionIdListsTailNode = rowsMap.getTransactionIdListsTailNode(lockIndex);
            }

            if (DEBUG) {

                debug("add statementId", b -> b.add("addStatementId", addStatementId).binary("statementValue", statementValue)
                        .add("statementIdsListsHeadNode", statementIdListsHeadNode).add("statementIdListsTailNode", statementIdListsHeadNode));
            }

            final long updatedStatementIdListsHeadNode;
            final long updatedStatementIdListsTailNode;

            if (addStatementId) {

                statementIdLists.addHead(this, statementValue, statementIdListsHeadNode, statementIdListsTailNode, LockTable::setScratchHeadNode, LockTable::setScratchTailNode);

                updatedStatementIdListsHeadNode = scratchHeadNode;
                updatedStatementIdListsTailNode = scratchTailNode;
            }
            else {
                updatedStatementIdListsHeadNode = statementIdListsHeadNode;
                updatedStatementIdListsTailNode = statementIdListsTailNode;
            }

            rowsMap.put(key, updatedLockBits, updatedTransactionIdListsHeadNode, updatedTransactionIdListsTailNode, updatedStatementIdListsHeadNode,
                    updatedStatementIdListsTailNode);
        }

        if (DEBUG) {

            exit(canLock);
        }

        return canLock;
    }

    private void unlock(long transactionId, int statementId, int tableId, long rowId, LockType lockType) throws NotLockedException {

        checkParameters(transactionId, statementId, tableId, rowId);

        if (DEBUG) {

            enter(b -> b.add("transactionId", transactionId).add("statementId", statementId).add("tableId", tableId).add("rowId", rowId).add("lockType", lockType));
        }

        final long key = makeKey(tableId, rowId);

        final long lockIndex = rowsMap.getLockIndex(key);

        final long lockBits = rowsMap.getLock(lockIndex);

        if (!hasLocks(lockBits)) {

            throw new NotLockedException();
        }

        final long updatedLockBits = decrement(lockBits, lockType);

        final long transactionIdListsHeadNode = rowsMap.getTransactionIdListsHeadNode(lockIndex);
        final long transactionIdListsTailNode = rowsMap.getTransactionIdListsTailNode(lockIndex);

        final long statementIdListsHeadNode = rowsMap.getStatementIdListsHeadNode(lockIndex);
        final long statementIdListsTailNode = rowsMap.getStatementIdListsTailNode(lockIndex);

        final long transactionValue = transactionValue(transactionId, lockType);

        if (DEBUG) {

            debug("find transaction node", b -> b.binary("transactionValue", transactionValue).add("transactionIdListsHeadNode", transactionIdListsHeadNode));
        }

        final long transactionIdNode = transactionIdLists.findNode(transactionValue, transactionIdListsHeadNode);

        if (transactionIdNode == NO_NODE) {

            throw new NotLockedException();
        }

        transactionIdLists.removeNode(this, transactionIdNode, transactionIdListsHeadNode, transactionIdListsTailNode, LockTable::setScratchHeadNode,
                LockTable::setScratchTailNode);

        final long updatedTransactionIdListsHeadNode = scratchHeadNode;
        final long updatedTransactionIdListsTailNode = scratchTailNode;

        final int statementValue = statementValue(statementId, lockType);

        if (DEBUG) {

            debug("find statement node", b -> b.binary("statementValue", statementValue).add("statementIdListsHeadNode", statementIdListsHeadNode));
        }

        final long statementIdNode = statementIdLists.findNode(statementValue, statementIdListsHeadNode);

        if (statementIdNode == NO_NODE) {

            throw new NotLockedException();
        }

        statementIdLists.removeNode(this, statementIdNode, statementIdListsHeadNode, statementIdListsTailNode, LockTable::setScratchHeadNode, LockTable::setScratchTailNode);

        final long updatedStatementIdListsHeadNode = scratchHeadNode;
        final long updatedStatementIdListsTailNode = scratchTailNode;

        if (hasLocks(updatedLockBits)) {

            rowsMap.put(key, updatedLockBits, updatedTransactionIdListsHeadNode, updatedTransactionIdListsTailNode, updatedStatementIdListsHeadNode,
                    updatedStatementIdListsTailNode);
        }
        else {
            rowsMap.remove(key);
        }

        if (DEBUG) {

            exit();
        }
    }

    public synchronized LockedRows getLockedRows() {

        return rowsMap.getLockedRows();
    }

    public synchronized LockHolders getLockHolders(int tableId, long rowId) {

        final long key = makeKey(tableId, rowId);

        return rowsMap.getLockHolders(key, transactionIdLists, statementIdLists);
    }

    private void setScratchHeadNode(long scratchHeadNode) {
        this.scratchHeadNode = scratchHeadNode;
    }

    private void setScratchTailNode(long scratchTailNode) {
        this.scratchTailNode = scratchTailNode;
    }

    private static long transactionValue(long transactionId, LockType lockType) {

        return (((long)lockType.ordinal()) << TRANSACTION_VALUE_LOCK_TYPE_SHIFT) | transactionId;
    }

    static long transactionId(long transactionValue) {

        return (transactionValue & TRANSACTION_VALUE_ID_MASK) >>> TRANSACTION_VALUE_ID_SHIFT;
    }

    static LockType transactionLockType(long transactionValue) {

        final int ordinal = Integers.checkUnsignedLongToUnsignedInt((transactionValue & TRANSACTION_VALUE_LOCK_TYPE_MASK) >>> TRANSACTION_VALUE_LOCK_TYPE_SHIFT);

        return LockType.ofOrdinal(ordinal);
    }

    private static int statementValue(int statementId, LockType lockType) {

        return (lockType.ordinal() << STATEMENT_VALUE_LOCK_TYPE_SHIFT) | statementId;
    }

    static int statementId(int statementValue) {

        return (statementValue & STATEMENT_VALUE_ID_MASK) >>> STATEMENT_VALUE_ID_SHIFT;
    }

    static LockType statementLockType(int statementValue) {

        final int ordinal = (statementValue & STATEMENT_VALUE_LOCK_TYPE_MASK) >>> STATEMENT_VALUE_LOCK_TYPE_SHIFT;

        return LockType.ofOrdinal(ordinal);
    }

    private static long makeNone() {

        return 0L;
    }

    private static long makeOne(int shift) {

        return 1L << shift;
    }

    private static long increment(long lockBits, LockType lockType) {

        return increment(lockBits, lockType.mask(), lockType.shift());
    }

    private static long increment(long lockBits, long mask, int shift) {

        final long numReadLocked = (lockBits & mask) >>> shift;
        final long updatedLockBits = (numReadLocked + 1) << shift;

        final long result = (lockBits & (~mask)) | updatedLockBits;

        if (DEBUG) {

            PrintDebug.debug(debugClass, "increment", b -> b.binary("result", result).binary("lockBits", lockBits).binary("mask", mask).add("shift", shift)
                    .binary("updatedLockBits", updatedLockBits));
        }

        return result;
    }

    private static long decrement(long lockBits, LockType lockType) {

        return decrement(lockBits, lockType.mask(), lockType.shift());
    }

    private static long decrement(long lockBits, long mask, int shift) {

        final long numLocked = (lockBits & mask) >>> shift;
        final long updatedLockBits = (numLocked - 1) << shift;

        final long result = (lockBits & (~mask)) | updatedLockBits;

        if (DEBUG) {

            PrintDebug.debug(debugClass, "decrement", b -> b.binary("result", result).binary("lockBits", lockBits).binary("mask", mask).add("shift", shift)
                    .add("numLocked", numLocked).binary("updatedLockBits", updatedLockBits));
        }

        return result;
    }

    private static boolean hasLocks(long lockBits) {

        return (lockBits & ALL_NUM_LOCKED_BITS_MASK) != 0L;
    }

    private static int numLocks(long lockBits, LockType lockType) {

        return (int)((lockBits & lockType.mask()) >>> lockType.shift());
    }

    private static void checkParameters(long transactionId, int statementId, int tableId, long rowId) {

        Checks.isTransactionId(transactionId);
        Checks.isStatementId(statementId);

        checkParameters(tableId, rowId);
    }
}

