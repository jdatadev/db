package dev.jdata.db.data.locktable;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.data.locktable.LockTable.LockType;
import dev.jdata.db.data.locktable.LockTable.NotLockedException;
import dev.jdata.db.test.unit.BaseTest;

public final class LockTableTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testReadLockAndUnlock() throws NotLockedException {

        checkLockAndUnlock(LockTable::tryReadLock, LockTable::readUnlock, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockAndUnlock() throws NotLockedException {

        checkLockAndUnlock(LockTable::tryWriteLock, LockTable::writeUnlock, LockType.WRITE);
    }

    @FunctionalInterface
    private interface LockFunction {

        boolean tryLock(LockTable lockTable, long transactionId, int statementId, int tableId, long rowId);
    }

    @FunctionalInterface
    private interface UnlockFunction {

        void unlock(LockTable lockTable, long transactionId, int statementId, int tableId, long rowId) throws NotLockedException;
    }

    private void checkLockAndUnlock(LockFunction lockFunction, UnlockFunction unlockFunction, LockType expectedLockType) throws NotLockedException {

        final LockTable lockTable = new LockTable();

        final long transactionId = 123L;
        final int statementId = 234;

        final int tableId = 345;
        final long rowId = 456L;

        final boolean locked = lockFunction.tryLock(lockTable, transactionId, statementId, tableId, rowId);

        assertThat(locked).isTrue();

        checkLockedRow(lockTable, tableId, rowId, expectedLockType == LockType.READ ? 1 : 0, expectedLockType == LockType.WRITE ? 1 : 0);

        LockHolders lockHolders = lockTable.getLockHolders(tableId, rowId);

        assertThat(lockHolders).isNotNull();
        assertThat(lockHolders.getNumTransactionValues()).isEqualTo(1);
        assertThat(lockHolders.getTransactionId(0)).isEqualTo(transactionId);
        assertThat(lockHolders.getTransactionLockType(0)).isEqualTo(expectedLockType);

        assertThat(lockHolders.getNumStatementValues()).isEqualTo(1);
        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
        assertThat(lockHolders.getStatementLockType(0)).isEqualTo(expectedLockType);

        unlockFunction.unlock(lockTable, transactionId, statementId, tableId, rowId);

        checkNotLockedRow(lockTable, tableId, rowId);

        lockHolders = lockTable.getLockHolders(tableId, rowId);

        assertThat(lockHolders).isNull();
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockDifferentTransactions() throws NotLockedException {

        final LockTable lockTable = new LockTable();

        final long transactionId1 = 123L;
        final long transactionId2 = 234L;
        final long transactionId3 = 345L;

        final int statementId = 456;

        final int tableId = 567;
        final long rowId = 678L;

        assertThat(lockTable.tryReadLock(transactionId1, statementId, tableId, rowId)).isTrue();
        assertThat(lockTable.tryReadLock(transactionId2, statementId, tableId, rowId)).isTrue();
        assertThat(lockTable.tryReadLock(transactionId3, statementId, tableId, rowId)).isTrue();

        checkLockedRow(lockTable, tableId, rowId, 3, 0);

        final LockHolders lockHolders = lockTable.getLockHolders(tableId, rowId);

        final LockType expectedLockType = LockType.READ;

        assertThat(lockHolders).isNotNull();

        final int numTransactionValues = 3;

        assertThat(lockHolders.getNumTransactionValues()).isEqualTo(numTransactionValues);

        final Set<Long> transationIds = new HashSet<>(numTransactionValues);

        for (int i = 0; i < numTransactionValues; ++ i) {

            transationIds.add(lockHolders.getTransactionId(i));

            assertThat(lockHolders.getTransactionLockType(i)).isEqualTo(expectedLockType);
        }

        assertThat(transationIds).containsExactlyInAnyOrder(transactionId1, transactionId2, transactionId3);

        assertThat(lockHolders.getNumStatementValues()).isEqualTo(1);
        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
        assertThat(lockHolders.getStatementLockType(0)).isEqualTo(expectedLockType);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockSameTransaction() {

        checkLockSameTransaction(LockTable::tryReadLock, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockSameTransaction() {

        checkLockSameTransaction(LockTable::tryWriteLock, LockType.WRITE);
    }

    private void checkLockSameTransaction(LockFunction lockFunction, LockType expectedLockType) {

        final LockTable lockTable = new LockTable();

        final long transactionId = 123L;

        final int statementId = 234;

        final int tableId = 345;
        final long rowId = 456L;

        assertThat(lockFunction.tryLock(lockTable, transactionId, statementId, tableId, rowId)).isTrue();
        assertThat(lockFunction.tryLock(lockTable, transactionId, statementId, tableId, rowId)).isTrue();
        assertThat(lockFunction.tryLock(lockTable, transactionId, statementId, tableId, rowId)).isTrue();

        checkLockedRow(lockTable, tableId, rowId, expectedLockType == LockType.READ ? 3 : 0, expectedLockType == LockType.WRITE ? 3 : 0);

        final LockHolders lockHolders = lockTable.getLockHolders(tableId, rowId);

        checkOneLockHolder(lockHolders, transactionId, statementId, expectedLockType);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadThenWriteDifferentTransactions() {

        checkLockTypeThenOtherLockTypeDifferentTransactions(LockTable::tryReadLock, LockTable::tryWriteLock, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteThenReadDifferentTransactions() {

        checkLockTypeThenOtherLockTypeDifferentTransactions(LockTable::tryWriteLock, LockTable::tryReadLock, LockType.WRITE);
    }

    private void checkLockTypeThenOtherLockTypeDifferentTransactions(LockFunction lockFunction, LockFunction otherLockFunction, LockType expectedLockType) {

        final LockTable lockTable = new LockTable();

        final long transactionId1 = 123L;
        final long transactionId2 = 234L;

        final int statementId = 345;

        final int tableId = 456;
        final long rowId = 567L;

        assertThat(lockFunction.tryLock(lockTable, transactionId1, statementId, tableId, rowId)).isTrue();
        assertThat(otherLockFunction.tryLock(lockTable, transactionId2, statementId, tableId, rowId)).isFalse();

        checkLockedRow(lockTable, tableId, rowId, expectedLockType == LockType.READ ? 1 : 0, expectedLockType == LockType.WRITE ? 1 : 0);

        final LockHolders lockHolders = lockTable.getLockHolders(tableId, rowId);

        assertThat(lockHolders).isNotNull();

        assertThat(lockHolders.getNumTransactionValues()).isEqualTo(1);
        assertThat(lockHolders.getTransactionId(0)).isEqualTo(transactionId1);
        assertThat(lockHolders.getTransactionLockType(0)).isEqualTo(expectedLockType);

        assertThat(lockHolders.getNumStatementValues()).isEqualTo(1);
        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
        assertThat(lockHolders.getStatementLockType(0)).isEqualTo(expectedLockType);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadThenWriteSameTransactions() {

        checkLockTypeThenOtherLockTypeSameTransaction(LockTable::tryReadLock, LockTable::tryWriteLock);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteThenReadSameTransactions() {

        checkLockTypeThenOtherLockTypeSameTransaction(LockTable::tryWriteLock, LockTable::tryReadLock);
    }

    private void checkLockTypeThenOtherLockTypeSameTransaction(LockFunction lockFunction, LockFunction otherLockFunction) {

        final LockTable lockTable = new LockTable();

        final long transactionId = 123L;

        final int statementId = 345;

        final int tableId = 456;
        final long rowId = 567L;

        assertThat(lockFunction.tryLock(lockTable, transactionId, statementId, tableId, rowId)).isTrue();
        assertThat(otherLockFunction.tryLock(lockTable, transactionId, statementId, tableId, rowId)).isTrue();

        checkLockedRow(lockTable, tableId, rowId, 1, 1);

        final LockHolders lockHolders = lockTable.getLockHolders(tableId, rowId);

        assertThat(lockHolders).isNotNull();

        final int numTransactionValues = 2;

        assertThat(lockHolders.getNumTransactionValues()).isEqualTo(numTransactionValues);

        final Set<LockType> transactionLockTypes = new HashSet<>(numTransactionValues);

        for (int i = 0; i < numTransactionValues; ++ i) {

            transactionLockTypes.add(lockHolders.getTransactionLockType(i));

            assertThat(lockHolders.getTransactionId(i)).isEqualTo(transactionId);
        }

        assertThat(transactionLockTypes).containsExactlyInAnyOrder(LockType.READ, LockType.WRITE);

        final int numStatementValues = 2;

        assertThat(lockHolders.getNumStatementValues()).isEqualTo(numStatementValues);

        final Set<LockType> statementLockTypes = new HashSet<>(numStatementValues);

        for (int i = 0; i < numStatementValues; ++ i) {

            statementLockTypes.add(lockHolders.getTransactionLockType(i));

            assertThat(lockHolders.getTransactionId(i)).isEqualTo(transactionId);
        }

        assertThat(statementLockTypes).containsExactlyInAnyOrder(LockType.READ, LockType.WRITE);
    }

    private static void checkLockedRow(LockTable lockTable, int tableId, long rowId, int expectedNumReadLocks, int expectedNumWriteLocks) {

        final LockedRows lockedRows = lockTable.getLockedRows();

        assertThat(lockedRows).isNotNull();
        assertThat(lockedRows).isNotEmpty();
        assertThat(lockedRows).hasNumElements(1L);
        assertThat(lockedRows.getTableId(0)).isEqualTo(tableId);
        assertThat(lockedRows.getRowId(0)).isEqualTo(rowId);
        assertThat(lockedRows.getNumLocks(0, LockType.READ)).isEqualTo(expectedNumReadLocks);
        assertThat(lockedRows.getNumLocks(0, LockType.WRITE)).isEqualTo(expectedNumWriteLocks);
    }

    private static void checkNotLockedRow(LockTable lockTable, int tableId, long rowId) {

        final LockedRows lockedRows = lockTable.getLockedRows();

        assertThat(lockedRows).isNotNull();
        assertThat(lockedRows).isEmpty();
        assertThat(lockedRows).hasNumElements(0L);
    }

    private static void checkOneLockHolder(LockHolders lockHolders, long transactionId, int statementId, LockType expectedLockType) {

        assertThat(lockHolders).isNotNull();
        assertThat(lockHolders.getNumTransactionValues()).isEqualTo(1);
        assertThat(lockHolders.getTransactionId(0)).isEqualTo(transactionId);
        assertThat(lockHolders.getTransactionLockType(0)).isEqualTo(expectedLockType);

        assertThat(lockHolders.getNumStatementValues()).isEqualTo(1);
        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
        assertThat(lockHolders.getStatementLockType(0)).isEqualTo(expectedLockType);
    }
}
