package dev.jdata.db.data.locktable;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DBException;
import dev.jdata.db.DebugConstants;
import dev.jdata.db.LockType;
import dev.jdata.db.data.BaseRows;
import dev.jdata.db.utils.adt.arrays.ILongArrayGetters;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadDoublyLinkedList;
import dev.jdata.db.utils.adt.lists.LongMultiList;
import dev.jdata.db.utils.adt.lists.LongMutableMultiList;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class LockTable extends BaseRows implements PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_LOCK_TABLE;

    private static final boolean ASSERT = AssertionContants.ASSERT_LOCK_TABLE;

    private static final Class<?> debugClass = LockTable.class;

    public static abstract class LockTableException extends DBException {

        private static final long serialVersionUID = 1L;
    }

    public static final class NotLockedException extends LockTableException {

        private static final long serialVersionUID = 1L;
    }

    private interface LockBitsGetter {

        long getNumReadLocks(LockTable lockTable, long lockIndex);
        long getNumWriteLocks(LockTable lockTable, long lockIndex);
    }

    private interface LockSetter extends LockBitsGetter {

        long getLockInfoListsHeadNode(LockTable lockTable, long lockIndex);
        long getLockInfoListsTailNode(LockTable lockTable, long lockIndex);

        void setLock(LockTable lockTable, int tableId, long rowId, long updatedNumReadLocks, long updatedNumWriteLocks, long updatedLockInfoListsHeadNode,
                long updatedLockInfoListsTailNode);

        void remove(LockTable lockTable, int tableId, long rowId);

        void increment(LockTable lockTable, int tableId, int transactionDescriptor, int statementId, LockType lockType);
        void decrement(LockTable lockTable, int tableId, int transactionDescriptor, int statementId, LockType lockType);
    }

    private static final long NO_NODE = BaseList.NO_NODE;

    private static final int READ_NUM_BITS = 32;
    static final int READ_SHIFT = 32;
    static final long READ_MASK = BitsUtil.maskLong(READ_NUM_BITS, READ_SHIFT);

    private static final int WRITE_NUM_BITS = 32;
    static final int WRITE_SHIFT = 0;
    static final long WRITE_MASK = BitsUtil.maskLong(WRITE_NUM_BITS, WRITE_SHIFT);

    private static final int LOCK_INFO_NUM_BITS = 64;

    private static final int LOCK_INFO_TRANSACTION_DESCRIPTOR_NUM_BITS = 24;
    private static final int LOCK_INFO_STATEMENT_ID_NUM_BITS = LOCK_INFO_NUM_BITS - LOCK_TYPE_NUM_BITS - LOCK_INFO_TRANSACTION_DESCRIPTOR_NUM_BITS;

    private static final int LOCK_INFO_LOCK_TYPE_SHIFT = LOCK_INFO_NUM_BITS - LOCK_TYPE_NUM_BITS;
    private static final long LOCK_INFO_LOCK_TYPE_MASK = BitsUtil.maskLong(LOCK_TYPE_NUM_BITS, LOCK_INFO_LOCK_TYPE_SHIFT);

    private static final int LOCK_INFO_TRANSACTION_DESCRIPTOR_SHIFT = LOCK_INFO_LOCK_TYPE_SHIFT - LOCK_INFO_TRANSACTION_DESCRIPTOR_NUM_BITS;
    private static final long LOCK_INFO_TRANSACTION_DESCRIPTOR_MASK = BitsUtil.maskLong(LOCK_INFO_TRANSACTION_DESCRIPTOR_NUM_BITS, LOCK_INFO_TRANSACTION_DESCRIPTOR_SHIFT);

    private static final long LOCK_INFO_STATEMENT_ID_MASK = BitsUtil.maskLong(LOCK_INFO_STATEMENT_ID_NUM_BITS, 0);

    private final int[] tableNumReadLocks;
    private final int[] tableNumWriteLocks;
    private final long[] tableLockInfoListsHeadNodes;
    private final long[] tableLockInfoListsTailNodes;

    private final long[][] tableNumRowLocks;

    @Deprecated // scalability issue, [tableId, transactionDescriptor] ref count map instead
    private final long[] tableRowLocksLockInfoListsHeadNodes;
    private final long[] tableRowLocksLockInfoListsTailNodes;

    private final ILockTableRowsMap rowsMap;

    private final LargeLongMultiHeadDoublyLinkedList<LockTable> lockInfoLists;

    private final LockSetter tableLockSetter;

    private final LockSetter rowLockSetter;

    private final LargeLongArray scratchLockIndices;

    private long scratchHeadNode;
    private long scratchTailNode;

    public LockTable(int numTables) {

        Checks.isNumElements(numTables);

        this.tableNumReadLocks = new int[numTables];
        this.tableNumWriteLocks = new int[numTables];
        this.tableLockInfoListsHeadNodes = new long[numTables];
        this.tableLockInfoListsTailNodes = new long[numTables];

        Arrays.fill(tableNumReadLocks, 0);
        Arrays.fill(tableNumWriteLocks, 0);
        Arrays.fill(tableLockInfoListsHeadNodes, NO_NODE);
        Arrays.fill(tableLockInfoListsTailNodes, NO_NODE);

        final int numLockTypes = LockType.values().length;

        this.tableNumRowLocks = new long[numLockTypes][];

        for (int i = 0; i < numLockTypes; ++ i) {

            final long[] numRowLocksForLockType = new long[numTables];

            Arrays.fill(numRowLocksForLockType, 0L);

            tableNumRowLocks[i] = numRowLocksForLockType;
        }

        this.tableRowLocksLockInfoListsHeadNodes = new long[numTables];
        this.tableRowLocksLockInfoListsTailNodes = new long[numTables];

        Arrays.fill(tableRowLocksLockInfoListsHeadNodes, NO_NODE);
        Arrays.fill(tableRowLocksLockInfoListsTailNodes, NO_NODE);

        final int initialCapacityExponent = 0;

        this.rowsMap = new LockTableRowsMap(initialCapacityExponent);

        final int initialOuterCapacity = 1;
        final int innerCapacity = 10000;

        this.lockInfoLists = new LargeLongMultiHeadDoublyLinkedList<>(initialOuterCapacity, innerCapacity);

        this.tableLockSetter = new TableLockSetter();

        this.rowLockSetter = new RowLockSetter();

        this.scratchLockIndices = new LargeLongArray(initialOuterCapacity, innerCapacity);
    }

    public boolean tryReadLockTable(int tableId, int transactionDescriptor, int statementId) {

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId));
        }

        checkParameters(tableId, transactionDescriptor, statementId);

        final boolean result = tryLockTable(tableId, transactionDescriptor, statementId, LockType.READ);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    public boolean tryWriteLockTable(int tableId, int transactionDescriptor, int statementId) {

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId));
        }

        checkParameters(tableId, transactionDescriptor, statementId);

        final boolean result = tryLockTable(tableId, transactionDescriptor, statementId, LockType.WRITE);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    public synchronized boolean tryLockTable(int tableId, int transactionDescriptor, int statementId, LockType lockType) {

        checkParameters(tableId, transactionDescriptor, statementId, lockType);

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId).add("lockType", lockType));
        }

        final boolean canLockAtTableLevel = canLockAtTableLevel(tableId, transactionDescriptor, lockType);

        final boolean lockAquired;

        if (canLockAtTableLevel) {

            final boolean canLockAllRows = canLock(lockInfoLists, transactionDescriptor, lockType, tableNumRowLocks[LockType.READ.ordinal()][tableId],
                    tableNumRowLocks[LockType.WRITE.ordinal()][tableId], tableRowLocksLockInfoListsHeadNodes[tableId]);

            if (canLockAllRows) {

                lock(this, tableId, -1L, transactionDescriptor, statementId, lockType, tableId, tableLockSetter);

                lockAquired = true;
            }
            else {
                lockAquired = false;
            }
        }
        else {
            lockAquired = false;
        }

        if (DEBUG) {

            exit(lockAquired);
        }

        return lockAquired;
    }

    public void readUnlockTable(int tableId, int transactionDescriptor, int statementId) throws NotLockedException {

        checkParameters(tableId, transactionDescriptor, statementId);

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId));
        }

        unlockTable(tableId, transactionDescriptor, statementId, LockType.READ);

        if (DEBUG) {

            exit();
        }
    }

    public void writeUnlockTable(int tableId, int transactionDescriptor, int statementId) throws NotLockedException {

        checkParameters(tableId, transactionDescriptor, statementId);

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId));
        }

        unlockTable(tableId, transactionDescriptor, statementId, LockType.WRITE);

        if (DEBUG) {

            exit();
        }
    }

    public synchronized void unlockTable(int tableId, int transactionDescriptor, int statementId, LockType lockType) throws NotLockedException {

        checkParameters(tableId, transactionDescriptor, statementId, lockType);

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId).add("lockType", lockType));
        }

        final long tableLevelNumReadLocks = tableNumReadLocks[tableId];
        final long tableLevelNumWriteLocks = tableNumWriteLocks[tableId];

        unlock(this, tableId, -1L, transactionDescriptor, statementId, lockType, tableId, tableLevelNumReadLocks, tableLevelNumWriteLocks, tableLockSetter);

        if (DEBUG) {

            exit();
        }
    }

    public boolean tryReadLockRow(int tableId, long rowId, int transactionDescriptor, int statementId) {

        return tryLockRow(tableId, rowId, transactionDescriptor, statementId, LockType.READ);
    }

    public void readUnlockRow(int tableId, long rowId, int transactionDescriptor, int statementId) throws NotLockedException {

        unlockRow(tableId, rowId, transactionDescriptor, statementId, LockType.READ);
    }

    public boolean tryWriteLockRow(int tableId, long rowId, int transactionDescriptor, int statementId) {

        return tryLockRow(tableId, rowId, transactionDescriptor, statementId, LockType.WRITE);
    }

    public void writeUnlockRow(int tableId, long rowId, int transactionDescriptor, int statementId) throws NotLockedException {

        unlockRow(tableId, rowId, transactionDescriptor, statementId, LockType.WRITE);
    }

    public synchronized boolean tryLockRows(int tableId, ILongArrayGetters rowIds, int transactionDescriptor, int statementId, LockType lockType) {

        checkParameters(tableId, transactionDescriptor, statementId, lockType);
        Objects.requireNonNull(rowIds);

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("rowIds.getNumElements()", rowIds.getNumElements()).add("transactionDescriptor", transactionDescriptor)
                    .add("statementId", statementId).add("lockType", lockType));
        }

        boolean allLocksAquired = true;

        final long numRows = rowIds.getNumElements();

        if (ASSERT) {

            Assertions.isEmpty(scratchLockIndices);
        }

        try {
            for (long i = 0; i < numRows; ++ i) {

                final long key = makeHashKey(tableId, rowIds.get(i));

                final long lockIndex = rowsMap.getLockIndex(key);

                final boolean canLock = canLockRow(tableId, rowIds.get(i), transactionDescriptor, lockType, lockIndex);

                if (!canLock) {

                    allLocksAquired = false;
                    break;
                }

                scratchLockIndices.add(lockIndex);
            }

            if (allLocksAquired) {

                for (long i = 0; i < numRows; ++ i) {

                    lockRow(tableId, rowIds.get(i), transactionDescriptor, statementId, lockType, scratchLockIndices.get(i));
                }
            }
        }
        finally {

            scratchLockIndices.clear();
        }

        return allLocksAquired;
    }

    public synchronized boolean tryLockRow(int tableId, long rowId, int transactionDescriptor, int statementId, LockType lockType) {

        checkParameters(tableId, rowId, transactionDescriptor, statementId);

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("rowId", rowId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId)
                    .add("lockType", lockType));
        }

        final long key = makeHashKey(tableId, rowId);

        final long lockIndex = rowsMap.getLockIndex(key);

        final boolean canLock;

        if (canLockAtTableLevel(tableId, transactionDescriptor, lockType)) {

            canLock = canLockRow(tableId, rowId, transactionDescriptor, lockType, lockIndex);

            if (canLock) {

                lockRow(tableId, rowId, transactionDescriptor, statementId, lockType, lockIndex);
            }
        }
        else {
            canLock = false;
        }

        if (DEBUG) {

            exit(canLock);
        }

        return canLock;
    }

    private boolean canLockAtTableLevel(int tableId, int transactionDescriptor, LockType lockType) {

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("transactionDescriptor", transactionDescriptor).add("lockType", lockType));
        }

        final boolean result = canLock(lockInfoLists, transactionDescriptor, lockType, tableNumReadLocks[tableId], tableNumWriteLocks[tableId],
                tableLockInfoListsHeadNodes[tableId]);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private boolean canLockRow(int tableId, long rowId, int transactionDescriptor, LockType lockType, long lockIndex) {

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("rowId", rowId).add("transactionDescriptor", transactionDescriptor).add("lockType", lockType).add("lockIndex", lockIndex));
        }

        final boolean result;

        if (lockIndex == ILockTableRowsMap.NO_LOCK_INDEX) {

            result = true;
        }
        else {
            final long lockBits = rowsMap.getLock(lockIndex);

            final long lockInfoListsHeadNode = rowsMap.getLockInfoListsHeadNode(lockIndex);

            result = canLock(lockInfoLists, transactionDescriptor, lockType, numReadLocks(lockBits), numWriteLocks(lockBits), lockInfoListsHeadNode);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private static boolean canLock(LongMultiList lockInfoLists, int transactionDescriptor, LockType lockType, long numReadLocks, long numWriteLocks,
            long lockInfoListsHeadNode) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("transactionDescriptor", transactionDescriptor).add("lockType", lockType).add("numReadLocks", numReadLocks)
                    .add("numWriteLocks", numWriteLocks).binary("lockInfoListsHeadNode", lockInfoListsHeadNode));
        }

        final boolean canLock;

        if (numWriteLocks > 0) {

            if (lockInfoListsHeadNode == NO_NODE) {

                throw new IllegalStateException();
            }
            else {
                canLock = lockInfoLists.containsOnly(transactionDescriptor, lockInfoListsHeadNode, (i, l) -> i == transactionDescriptor(l));
            }
        }
        else {
            switch (lockType) {

            case READ:

                canLock = true;
                break;

            case WRITE:

                if (numReadLocks > 0) {

                    if (lockInfoListsHeadNode == NO_NODE) {

                        throw new IllegalStateException();
                    }
                    else {
                        canLock = lockInfoLists.containsOnly(transactionDescriptor, lockInfoListsHeadNode, (i, l) -> i == transactionDescriptor(l));
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

        if (DEBUG) {

            PrintDebug.exit(debugClass, canLock);
        }

        return canLock;
    }

    private void lockRow(int tableId, long rowId, int transactionDescriptor, int statementId, LockType lockType, long lockIndex) {

        lock(this, tableId, rowId, transactionDescriptor, statementId, lockType, lockIndex, rowLockSetter);

        rowLockSetter.increment(this, tableId, transactionDescriptor, statementId, lockType);
    }

    private static void lock(LockTable lockTable, int tableId, long rowId, int transactionDescriptor, int statementId, LockType lockType, long lockIndex,
            LockSetter lockSetter) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("tableId", tableId).add("rowId", rowId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId)
                    .add("lockType", lockType).add("lockIndex", lockIndex));
        }

        final long lockInfoListsHeadNode;
        final long lockInfoListsTailNode;

        final boolean addLockInfo;

        final long numReadLocks;
        final long numWriteLocks;

        final long lockInfoValue = lockInfoValue(transactionDescriptor, statementId, lockType);

        if (lockIndex == ILockTableRowsMap.NO_LOCK_INDEX) {

            numReadLocks = 0L;
            numWriteLocks = 0L;

            lockInfoListsHeadNode = NO_NODE;
            lockInfoListsTailNode = NO_NODE;

            addLockInfo = true;
        }
        else {
            numReadLocks = lockSetter.getNumReadLocks(lockTable, lockIndex);
            numWriteLocks = lockSetter.getNumWriteLocks(lockTable, lockIndex);

            lockInfoListsHeadNode = lockSetter.getLockInfoListsHeadNode(lockTable, lockIndex);
            lockInfoListsTailNode = lockSetter.getLockInfoListsTailNode(lockTable, lockIndex);

            addLockInfo = !lockTable.lockInfoLists.contains(lockInfoValue, lockInfoListsHeadNode);
        }

        final long updatedNumReadLocks;
        final long updatedNumWriteLocks;

        switch (lockType) {

        case READ:

            updatedNumReadLocks = numReadLocks + 1L;
            updatedNumWriteLocks = numWriteLocks;
            break;

        case WRITE:

            updatedNumReadLocks = numReadLocks;
            updatedNumWriteLocks = numWriteLocks + 1L;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        final long updatedLockInfoListsHeadNode;
        final long updatedLockInfoListsTailNode;

        if (DEBUG) {

            PrintDebug.debug(debugClass, "add lockInfo", b -> b.add("numReadLocks", numReadLocks).add("numWriteLocks", numWriteLocks).binary("lockInfoValue", lockInfoValue)
                    .add("addLockInfo", addLockInfo).add("updatedNumReadLocks", updatedNumReadLocks).add("updatedNumWriteLocks", updatedNumWriteLocks)
                    .add("lockInfoListsHeadNode", lockInfoListsHeadNode).add("lockInfoListsTailNode", lockInfoListsTailNode));
        }

        if (addLockInfo) {

            lockTable.lockInfoLists.addHead(lockTable, lockInfoValue, lockInfoListsHeadNode, lockInfoListsTailNode, LockTable::setScratchHeadNode,
                    LockTable::setScratchTailNode);

            updatedLockInfoListsHeadNode = lockTable.scratchHeadNode;
            updatedLockInfoListsTailNode = lockTable.scratchTailNode;
        }
        else {
            updatedLockInfoListsHeadNode = lockInfoListsHeadNode;
            updatedLockInfoListsTailNode = lockSetter.getLockInfoListsTailNode(lockTable, lockIndex);
        }

        lockSetter.setLock(lockTable, tableId, rowId, updatedNumReadLocks, updatedNumWriteLocks, updatedLockInfoListsHeadNode, updatedLockInfoListsTailNode);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    public synchronized void unlockRow(int tableId, long rowId, int transactionDescriptor, int statementId, LockType lockType) throws NotLockedException {

        checkParameters(tableId, rowId, transactionDescriptor, statementId, lockType);

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("rowId", rowId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId)
                    .add("lockType", lockType));
        }

        final long key = makeHashKey(tableId, rowId);

        final long lockIndex = rowsMap.getLockIndex(key);

        final long lockBits = rowsMap.getLock(lockIndex);

        unlock(this, tableId, rowId, transactionDescriptor, statementId, lockType, lockIndex, numReadLocks(lockBits), numWriteLocks(lockBits), rowLockSetter);

        if (DEBUG) {

            exit();
        }
    }

    private static void unlock(LockTable lockTable, int tableId, long rowId, int transactionDescriptor, int statementId, LockType lockType, long lockIndex, long numReadLocks,
            long numWriteLocks, LockSetter lockSetter) throws NotLockedException {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("tableId", tableId).add("rowId", rowId).add("transactionDescriptor", transactionDescriptor).add("statementId", statementId)
                    .add("lockType", lockType).add("lockIndex", lockIndex).add("numReadLocks", numReadLocks).add("numWriteLocks", numWriteLocks));
        }

        if (!hasLocks(numReadLocks, numWriteLocks)) {

            throw new NotLockedException();
        }

        final long updatedNumReadLocks;
        final long updatedNumWriteLocks;

        switch (lockType) {

        case READ:

            updatedNumReadLocks = Checks.isAboveZero(numReadLocks) - 1L;
            updatedNumWriteLocks = Checks.isNotNegative(numWriteLocks);
            break;

        case WRITE:

            updatedNumReadLocks = Checks.isNotNegative(numReadLocks);
            updatedNumWriteLocks = Checks.isAboveZero(numWriteLocks) -1L;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        final long lockInfoListsHeadNode = lockSetter.getLockInfoListsHeadNode(lockTable, lockIndex);
        final long lockInfoListsTailNode = lockSetter.getLockInfoListsTailNode(lockTable, lockIndex);

        final long lockInfoValue = lockInfoValue(transactionDescriptor, statementId, lockType);

        if (DEBUG) {

            PrintDebug.debug(debugClass, "find lockInfo node", b -> b.binary("lockInfoValue", lockInfoValue).add("lockInfoListsHeadNode", lockInfoListsHeadNode));
        }

        final long lockInfoNode = lockTable.lockInfoLists.findNode(lockInfoValue, lockInfoListsHeadNode);

        if (lockInfoNode == NO_NODE) {

            throw new NotLockedException();
        }

        lockTable.lockInfoLists.removeNode(lockTable, lockInfoNode, lockInfoListsHeadNode, lockInfoListsTailNode, LockTable::setScratchHeadNode, LockTable::setScratchTailNode);

        final long updatedLockInfoListsHeadNode = lockTable.scratchHeadNode;
        final long updatedLockInfoListsTailNode = lockTable.scratchTailNode;

        if (hasLocks(updatedNumReadLocks, updatedNumWriteLocks)) {

            lockSetter.setLock(lockTable, tableId, rowId, updatedNumReadLocks, updatedNumWriteLocks, updatedLockInfoListsHeadNode, updatedLockInfoListsTailNode);
        }
        else {
            lockSetter.remove(lockTable, tableId, rowId);
        }

        lockSetter.decrement(lockTable, tableId, transactionDescriptor, statementId, lockType);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    public synchronized LockedTables getLockedTables() {

        return new LockedTables(tableNumReadLocks, tableNumWriteLocks);
    }

    public synchronized LockHolders getTableLockHolders(int tableId) {

        Checks.isTableId(tableId);

        return getLockHolders(tableLockInfoListsHeadNodes[tableId]);
    }

    public synchronized LockedRows getLockedRows() {

        return rowsMap.getLockedRows();
    }

    public synchronized LockHolders getRowLockHolders(int tableId, long rowId) {

        checkParameters(tableId, rowId);

        final long key = makeHashKey(tableId, rowId);

        return getLockHolders(rowsMap.getLockHoldersHeadNode(key));
    }

    private LockHolders getLockHolders(long lockInfoHeadNode) {

        return lockInfoHeadNode != NO_NODE
                ? new LockHolders(lockInfoLists.toArray(lockInfoHeadNode))
                : null;
    }

    private void setScratchHeadNode(long scratchHeadNode) {

        this.scratchHeadNode = scratchHeadNode;
    }

    private void setScratchTailNode(long scratchTailNode) {

        this.scratchTailNode = scratchTailNode;
    }

    private static long lockInfoValue(int transactionDescriptor, int statementId, LockType lockType) {

        return (((long)lockType.ordinal()) << LOCK_INFO_LOCK_TYPE_SHIFT) | (((long)transactionDescriptor) << LOCK_INFO_TRANSACTION_DESCRIPTOR_SHIFT) | statementId;
    }

    static int transactionDescriptor(long lockInfoValue) {

        return Integers.checkUnsignedLongToUnsignedInt((lockInfoValue & LOCK_INFO_TRANSACTION_DESCRIPTOR_MASK) >>> LOCK_INFO_TRANSACTION_DESCRIPTOR_SHIFT);
    }

    static LockType transactionLockType(long lockInfoValue) {

        final int ordinal = Integers.checkUnsignedLongToUnsignedInt((lockInfoValue & LOCK_INFO_LOCK_TYPE_MASK) >>> LOCK_INFO_LOCK_TYPE_SHIFT);

        return LockType.ofOrdinal(ordinal);
    }

    static int statementId(long lockInfoValue) {

        return Integers.checkUnsignedLongToUnsignedInt(lockInfoValue & LOCK_INFO_STATEMENT_ID_MASK);
    }

    private static boolean hasLocks(long numReadLocks, long numWriteLocks) {

        Checks.isNotNegative(numReadLocks);
        Checks.isNotNegative(numWriteLocks);

        return numReadLocks != 0L || numWriteLocks != 0L;
    }

    private static long makeLockBits(long numReadLocks, long numWriteLocks) {

        return makeLockBits(Integers.checkUnsignedLongToUnsignedInt(numReadLocks), Integers.checkUnsignedLongToUnsignedInt(numWriteLocks));
    }

    private static long makeLockBits(int numReadLocks, int numWriteLocks) {

        return (((long)numReadLocks) << shift(LockType.READ)) | (((long)numWriteLocks) << shift(LockType.WRITE));
    }

    private static int numReadLocks(long lockBits) {

        return numLocks(lockBits, LockType.READ);
    }

    private static int numWriteLocks(long lockBits) {

        return numLocks(lockBits, LockType.WRITE);
    }

    private static int numLocks(long lockBits, LockType lockType) {

        return (int)((lockBits & mask(lockType)) >>> shift(lockType));
    }

    private static void checkParameters(int tableId, int transactionDescriptor, int statementId) {

        Checks.isTableId(tableId);
        Checks.isTransactionDescriptor(transactionDescriptor);
        Checks.isStatementId(statementId);
    }

    private static void checkParameters(int tableId, int transactionDescriptor, int statementId, LockType lockType) {

        checkParameters(tableId, transactionDescriptor, statementId);

        Objects.requireNonNull(lockType);
    }

    private static void checkParameters(int tableId, long rowId, int transactionDescriptor, int statementId) {

        checkParameters(tableId, transactionDescriptor, statementId);

        Checks.isRowId(rowId);
    }

    private static void checkParameters(int tableId, long rowId, int transactionDescriptor, int statementId, LockType lockType) {

        checkParameters(tableId, rowId, transactionDescriptor, statementId);

        Objects.requireNonNull(lockType);
    }

    static long mask(LockType lockType) {

        final long result;

        switch (lockType) {

        case READ:

            result = LockTable.READ_MASK;
            break;

        case WRITE:

            result = LockTable.WRITE_MASK;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    static int shift(LockType lockType) {

        final int result;

        switch (lockType) {

        case READ:

            result = LockTable.READ_SHIFT;
            break;

        case WRITE:

            result = LockTable.WRITE_SHIFT;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    private static abstract class TableLockBitsGetter implements LockBitsGetter {

        @Override
        public long getNumReadLocks(LockTable lockTable, long lockIndex) {

            return lockTable.tableNumReadLocks[IElements.intIndex(lockIndex)];
        }

        @Override
        public long getNumWriteLocks(LockTable lockTable, long lockIndex) {

            return lockTable.tableNumWriteLocks[IElements.intIndex(lockIndex)];
        }
    }

    private static final class TableLockSetter extends TableLockBitsGetter implements LockSetter, PrintDebug {

        @Override
        public long getLockInfoListsHeadNode(LockTable lockTable, long lockIndex) {

            return lockTable.tableLockInfoListsHeadNodes[IElements.intIndex(lockIndex)];
        }

        @Override
        public long getLockInfoListsTailNode(LockTable lockTable, long lockIndex) {

            return lockTable.tableLockInfoListsTailNodes[IElements.intIndex(lockIndex)];
        }

        @Override
        public void setLock(LockTable lockTable, int tableId, long rowId, long updatedNumReadLocks, long updatedNumWriteLocks, long updatedLockInfoListsHeadNode,
                long updatedLockInfoListsTailNode) {

            lockTable.tableNumReadLocks[tableId] = Integers.checkUnsignedLongToUnsignedInt(updatedNumReadLocks);
            lockTable.tableNumWriteLocks[tableId] = Integers.checkUnsignedLongToUnsignedInt(updatedNumWriteLocks);

            lockTable.tableLockInfoListsHeadNodes[tableId] = updatedLockInfoListsHeadNode;
            lockTable.tableLockInfoListsTailNodes[tableId] = updatedLockInfoListsTailNode;
        }

        @Override
        public void remove(LockTable lockTable, int tableId, long rowId) {

            final int numReadLocks = lockTable.tableNumReadLocks[tableId];
            final int numWriteLocks = lockTable.tableNumWriteLocks[tableId];

            if (DEBUG) {

                debug("table lock setter remove", b -> b.add("tableId", tableId).add("numReadLocks", numReadLocks).add("numWriteLocks", numWriteLocks));
            }

            if (ASSERT) {

                Assertions.isTrue((numReadLocks == 1 && numWriteLocks == 0) || (numReadLocks == 0 && numWriteLocks == 1));
            }

            if (numReadLocks == 1) {

                lockTable.tableNumReadLocks[tableId] = 0;
            }
            else if (numWriteLocks == 1) {

                lockTable.tableNumWriteLocks[tableId] = 0;
            }

            lockTable.tableLockInfoListsHeadNodes[tableId] = NO_NODE;
            lockTable.tableLockInfoListsTailNodes[tableId] = NO_NODE;
        }

        @Override
        public void increment(LockTable lockTable, int tableId, int transactionDescriptor, int statementId, LockType lockType) {

        }

        @Override
        public void decrement(LockTable lockTable, int tableId, int transactionDescriptor, int statementId, LockType lockType) {

        }
    }

    private static abstract class RowLockBitsGetter implements LockBitsGetter {

        @Override
        public long getNumReadLocks(LockTable lockTable, long lockIndex) {

            return numReadLocks(getLockBits(lockTable, lockIndex));
        }

        @Override
        public long getNumWriteLocks(LockTable lockTable, long lockIndex) {

            return numWriteLocks(getLockBits(lockTable, lockIndex));
        }

        private static long getLockBits(LockTable lockTable, long lockIndex) {

            return lockTable.rowsMap.getLock(lockIndex);
        }
    }

    private static final class RowLockSetter extends RowLockBitsGetter implements LockSetter {

        @Override
        public long getLockInfoListsHeadNode(LockTable lockTable, long lockIndex) {

            return lockTable.rowsMap.getLockInfoListsHeadNode(lockIndex);
        }

        @Override
        public long getLockInfoListsTailNode(LockTable lockTable, long lockIndex) {

            return lockTable.rowsMap.getLockInfoListsTailNode(lockIndex);
        }

        @Override
        public void setLock(LockTable lockTable, int tableId, long rowId, long updatedNumReadLocks, long updatedNumWriteLocks, long updatedLockInfoListsHeadNode,
                long updatedLockInfoListsTailNode) {

            final long key = makeHashKey(tableId, rowId);

            final long updatedLockBits = makeLockBits(updatedNumReadLocks, updatedNumWriteLocks);

            lockTable.rowsMap.put(key, updatedLockBits, updatedLockInfoListsHeadNode, updatedLockInfoListsTailNode);
        }

        @Override
        public void remove(LockTable lockTable, int tableId, long rowId) {

            lockTable.rowsMap.remove(makeHashKey(tableId, rowId));
        }

        @Override
        public void increment(LockTable lockTable, int tableId, int transactionDescriptor, int statementId, LockType lockType) {

            ++ lockTable.tableNumRowLocks[lockType.ordinal()][tableId];

            final long lockInfoValue = lockInfoValue(transactionDescriptor, statementId, lockType);

            lockTable.lockInfoLists.addHead(lockTable, lockInfoValue,
                    lockTable.tableRowLocksLockInfoListsHeadNodes[tableId],
                    lockTable.tableRowLocksLockInfoListsTailNodes[tableId],
                    (t, n) -> lockTable.tableRowLocksLockInfoListsHeadNodes[tableId] = n,
                    (t, n) -> lockTable.tableRowLocksLockInfoListsTailNodes[tableId] = n);
        }

        @Override
        public void decrement(LockTable lockTable, int tableId, int transactionDescriptor, int statementId, LockType lockType) {

            final long[] lockArray = lockTable.tableNumRowLocks[lockType.ordinal()];

            if (ASSERT) {

                Assertions.isAboveZero(lockArray[tableId]);
            }

            -- lockArray[tableId];

            final LongMutableMultiList<LockTable> lockInfoLists = lockTable.lockInfoLists;

            final long lockInfoValue = lockInfoValue(transactionDescriptor, statementId, lockType);

            final long headNode = lockTable.tableRowLocksLockInfoListsHeadNodes[tableId];

            final long node = lockInfoLists.findNode(lockInfoValue, headNode);

            if (ASSERT) {

                Assertions.areNotEqual(node, NO_NODE);
            }

            lockInfoLists.removeNode(lockTable, node,
                    headNode,
                    lockTable.tableRowLocksLockInfoListsTailNodes[tableId],
                    (t, n) -> lockTable.tableRowLocksLockInfoListsHeadNodes[tableId] = n,
                    (t, n) -> lockTable.tableRowLocksLockInfoListsTailNodes[tableId] = n);
        }
    }
}
