package dev.jdata.db.data.locktable;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.LockType;
import dev.jdata.db.data.locktable.LockTable.NotLockedException;
import dev.jdata.db.test.unit.BaseTest;

public final class LockTableTest extends BaseTest {

    @FunctionalInterface
    private interface LockTableFunction {

        boolean tryLockTable(LockTable lockTable, int tableId, int transactionDescriptor, int statementId);
    }

    @FunctionalInterface
    private interface UnlockTableFunction {

        void unlockTable(LockTable lockTable, int tableId, int transactionDescriptor, int statementId) throws NotLockedException;
    }

    @FunctionalInterface
    private interface LockRowFunction {

        boolean tryLockRow(LockTable lockTable, int tableId, long rowId, int transactionDescriptor, int statementId);
    }

    @FunctionalInterface
    private interface UnlockRowFunction {

        void unlockRow(LockTable lockTable, int tableId, long rowId, int transactionDescriptor, int statementId) throws NotLockedException;
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockAndUnlockTable() throws NotLockedException {

        checkLockAndUnlockTable(LockTable::tryReadLockTable, LockTable::readUnlockTable, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockAndUnlockTable() throws NotLockedException {

        checkLockAndUnlockTable(LockTable::tryWriteLockTable, LockTable::writeUnlockTable, LockType.WRITE);
    }

    private void checkLockAndUnlockTable(LockTableFunction lockFunction, UnlockTableFunction unlockFunction, LockType expectedLockType) throws NotLockedException {

        final int tableId = 123;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        final boolean locked = lockFunction.tryLockTable(lockTable, tableId, transactionDescriptor, statementId);

        assertThat(locked).isTrue();

        checkLockedTable(lockTable, tableId, expectedLockType == LockType.READ ? 1 : 0, expectedLockType == LockType.WRITE ? 1 : 0);

        LockHolders lockHolders = lockTable.getTableLockHolders(tableId);

        assertThat(lockHolders).isNotNull();
        assertThat(lockHolders.getNumElements()).isEqualTo(1);
        assertThat(lockHolders.getTransactionDescriptor(0)).isEqualTo(transactionDescriptor);
        assertThat(lockHolders.getTransactionLockType(0)).isEqualTo(expectedLockType);

        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);

        unlockFunction.unlockTable(lockTable, tableId, transactionDescriptor, statementId);

        checkNotLockedTable(lockTable, tableId);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockAndUnlockRow() throws NotLockedException {

        checkLockAndUnlockRow(LockTable::tryReadLockRow, LockTable::readUnlockRow, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockAndUnlockRow() throws NotLockedException {

        checkLockAndUnlockRow(LockTable::tryWriteLockRow, LockTable::writeUnlockRow, LockType.WRITE);
    }

    private void checkLockAndUnlockRow(LockRowFunction lockFunction, UnlockRowFunction unlockFunction, LockType expectedLockType) throws NotLockedException {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        final boolean locked = lockFunction.tryLockRow(lockTable, tableId, rowId, transactionDescriptor, statementId);

        assertThat(locked).isTrue();

        checkLockedRow(lockTable, tableId, rowId, expectedLockType == LockType.READ ? 1 : 0, expectedLockType == LockType.WRITE ? 1 : 0);

        LockHolders lockHolders = lockTable.getRowLockHolders(tableId, rowId);

        assertThat(lockHolders).isNotNull();
        assertThat(lockHolders.getNumElements()).isEqualTo(1);
        assertThat(lockHolders.getTransactionDescriptor(0)).isEqualTo(transactionDescriptor);
        assertThat(lockHolders.getTransactionLockType(0)).isEqualTo(expectedLockType);

        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);

        unlockFunction.unlockRow(lockTable, tableId, rowId, transactionDescriptor, statementId);

        checkNotLockedRows(lockTable, tableId, rowId);

        lockHolders = lockTable.getRowLockHolders(tableId, rowId);

        assertThat(lockHolders).isNull();
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockTableDifferentTransactions() throws NotLockedException {

        final int tableId = 123;

        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int transactionDescriptor3 = 567;

        final int statementId = 678;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor2, statementId)).isTrue();
        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor3, statementId)).isTrue();

        checkLockedTable(lockTable, tableId, 3, 0);

        final LockHolders lockHolders = lockTable.getTableLockHolders(tableId);

        final LockType expectedLockType = LockType.READ;

        assertThat(lockHolders).isNotNull();

        final int numTransactionValues = 3;

        assertThat(lockHolders.getNumElements()).isEqualTo(numTransactionValues);

        final Set<Integer> transactionDescriptors = new HashSet<>(numTransactionValues);

        for (int i = 0; i < numTransactionValues; ++ i) {

            transactionDescriptors.add(lockHolders.getTransactionDescriptor(i));

            assertThat(lockHolders.getTransactionLockType(i)).isEqualTo(expectedLockType);
        }

        assertThat(transactionDescriptors).containsExactlyInAnyOrder(transactionDescriptor1, transactionDescriptor2, transactionDescriptor3);

        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockRowDifferentTransactions() throws NotLockedException {

        final int tableId = 123;
        final long rowId = 234L;

        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int transactionDescriptor3 = 567;

        final int statementId = 678;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor2, statementId)).isTrue();
        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor3, statementId)).isTrue();

        checkLockedRow(lockTable, tableId, rowId, 3, 0);

        final LockHolders lockHolders = lockTable.getRowLockHolders(tableId, rowId);

        final LockType expectedLockType = LockType.READ;

        assertThat(lockHolders).isNotNull();

        final int numTransactionValues = 3;

        assertThat(lockHolders.getNumElements()).isEqualTo(numTransactionValues);

        final Set<Integer> transactionDescriptors = new HashSet<>(numTransactionValues);

        for (int i = 0; i < numTransactionValues; ++ i) {

            transactionDescriptors.add(lockHolders.getTransactionDescriptor(i));

            assertThat(lockHolders.getTransactionLockType(i)).isEqualTo(expectedLockType);
        }

        assertThat(transactionDescriptors).containsExactlyInAnyOrder(transactionDescriptor1, transactionDescriptor2, transactionDescriptor3);

        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockTableSameTransaction() {

        checkLockTableSameTransaction(LockTable::tryReadLockTable, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockTableSameTransaction() {

        checkLockTableSameTransaction(LockTable::tryWriteLockTable, LockType.WRITE);
    }

    private void checkLockTableSameTransaction(LockTableFunction lockFunction, LockType expectedLockType) {

        final int tableId = 123;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockFunction.tryLockTable(lockTable, tableId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockFunction.tryLockTable(lockTable, tableId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockFunction.tryLockTable(lockTable, tableId, transactionDescriptor, statementId)).isTrue();

        checkLockedTable(lockTable, tableId, expectedLockType == LockType.READ ? 3 : 0, expectedLockType == LockType.WRITE ? 3 : 0);

        final LockHolders lockHolders = lockTable.getTableLockHolders(tableId);

        checkOneLockHolder(lockHolders, transactionDescriptor, statementId, expectedLockType);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockRowSameTransaction() {

        checkLockRowSameTransaction(LockTable::tryReadLockRow, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockRowSameTransaction() {

        checkLockRowSameTransaction(LockTable::tryWriteLockRow, LockType.WRITE);
    }

    private void checkLockRowSameTransaction(LockRowFunction lockFunction, LockType expectedLockType) {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockFunction.tryLockRow(lockTable, tableId, rowId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockFunction.tryLockRow(lockTable, tableId, rowId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockFunction.tryLockRow(lockTable, tableId, rowId, transactionDescriptor, statementId)).isTrue();

        checkLockedRow(lockTable, tableId, rowId, expectedLockType == LockType.READ ? 3 : 0, expectedLockType == LockType.WRITE ? 3 : 0);

        final LockHolders lockHolders = lockTable.getRowLockHolders(tableId, rowId);

        checkOneLockHolder(lockHolders, transactionDescriptor, statementId, expectedLockType);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadThenWriteLockTableDifferentTransactions() {

        checkLockTypeThenOtherLockTypeTableDifferentTransactions(LockTable::tryReadLockTable, LockTable::tryWriteLockTable, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteThenReadLockTableDifferentTransactions() {

        checkLockTypeThenOtherLockTypeTableDifferentTransactions(LockTable::tryWriteLockTable, LockTable::tryReadLockTable, LockType.WRITE);
    }

    private void checkLockTypeThenOtherLockTypeTableDifferentTransactions(LockTableFunction lockFunction, LockTableFunction otherLockFunction, LockType expectedLockType) {

        final int tableId = 123;

        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;

        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockFunction.tryLockTable(lockTable, tableId, transactionDescriptor1, statementId)).isTrue();
        assertThat(otherLockFunction.tryLockTable(lockTable, tableId, transactionDescriptor2, statementId)).isFalse();

        checkLockedTable(lockTable, tableId, expectedLockType == LockType.READ ? 1 : 0, expectedLockType == LockType.WRITE ? 1 : 0);

        final LockHolders lockHolders = lockTable.getTableLockHolders(tableId);

        assertThat(lockHolders).isNotNull();

        assertThat(lockHolders.getNumElements()).isEqualTo(1);
        assertThat(lockHolders.getTransactionDescriptor(0)).isEqualTo(transactionDescriptor1);
        assertThat(lockHolders.getTransactionLockType(0)).isEqualTo(expectedLockType);

        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadThenWriteLockRowDifferentTransactions() {

        checkLockTypeThenOtherLockTypeRowDifferentTransactions(LockTable::tryReadLockRow, LockTable::tryWriteLockRow, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteThenReadLockRowDifferentTransactions() {

        checkLockTypeThenOtherLockTypeRowDifferentTransactions(LockTable::tryWriteLockRow, LockTable::tryReadLockRow, LockType.WRITE);
    }

    private void checkLockTypeThenOtherLockTypeRowDifferentTransactions(LockRowFunction lockFunction, LockRowFunction otherLockFunction, LockType expectedLockType) {

        final int tableId = 123;
        final long rowId = 234L;

        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;

        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockFunction.tryLockRow(lockTable, tableId, rowId, transactionDescriptor1, statementId)).isTrue();
        assertThat(otherLockFunction.tryLockRow(lockTable, tableId, rowId, transactionDescriptor2, statementId)).isFalse();

        checkLockedRow(lockTable, tableId, rowId, expectedLockType == LockType.READ ? 1 : 0, expectedLockType == LockType.WRITE ? 1 : 0);

        final LockHolders lockHolders = lockTable.getRowLockHolders(tableId, rowId);

        assertThat(lockHolders).isNotNull();

        assertThat(lockHolders.getNumElements()).isEqualTo(1);
        assertThat(lockHolders.getTransactionDescriptor(0)).isEqualTo(transactionDescriptor1);
        assertThat(lockHolders.getTransactionLockType(0)).isEqualTo(expectedLockType);

        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadThenWriteTableSameTransactions() {

        checkLockTypeThenOtherLockTypeTableSameTransaction(LockTable::tryReadLockTable, LockTable::tryWriteLockTable);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteThenReadTableSameTransactions() {

        checkLockTypeThenOtherLockTypeTableSameTransaction(LockTable::tryWriteLockTable, LockTable::tryReadLockTable);
    }

    private void checkLockTypeThenOtherLockTypeTableSameTransaction(LockTableFunction lockFunction, LockTableFunction otherLockFunction) {

        final int tableId = 123;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockFunction.tryLockTable(lockTable, tableId, transactionDescriptor, statementId)).isTrue();
        assertThat(otherLockFunction.tryLockTable(lockTable, tableId, transactionDescriptor, statementId)).isTrue();

        checkLockedTable(lockTable, tableId, 1, 1);

        final LockHolders lockHolders = lockTable.getTableLockHolders(tableId);

        checkLockTypeThenOtherLockTypeSameTransactionLockHolders(lockHolders, transactionDescriptor);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadThenWriteRowSameTransactions() {

        checkLockTypeThenOtherLockTypeRowSameTransaction(LockTable::tryReadLockRow, LockTable::tryWriteLockRow);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteThenReadRowSameTransactions() {

        checkLockTypeThenOtherLockTypeRowSameTransaction(LockTable::tryWriteLockRow, LockTable::tryReadLockRow);
    }

    private void checkLockTypeThenOtherLockTypeRowSameTransaction(LockRowFunction lockFunction, LockRowFunction otherLockFunction) {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockFunction.tryLockRow(lockTable, tableId, rowId, transactionDescriptor, statementId)).isTrue();
        assertThat(otherLockFunction.tryLockRow(lockTable, tableId, rowId, transactionDescriptor, statementId)).isTrue();

        checkLockedRow(lockTable, tableId, rowId, 1, 1);

        final LockHolders lockHolders = lockTable.getRowLockHolders(tableId, rowId);

        checkLockTypeThenOtherLockTypeSameTransactionLockHolders(lockHolders, transactionDescriptor);
    }

    private static void checkLockTypeThenOtherLockTypeSameTransactionLockHolders(LockHolders lockHolders, int transactionDescriptor) {

        assertThat(lockHolders).isNotNull();

        final int numTransactionValues = 2;

        assertThat(lockHolders.getNumElements()).isEqualTo(numTransactionValues);

        final Set<LockType> transactionLockTypes = new HashSet<>(numTransactionValues);

        for (int i = 0; i < numTransactionValues; ++ i) {

            transactionLockTypes.add(lockHolders.getTransactionLockType(i));

            assertThat(lockHolders.getTransactionDescriptor(i)).isEqualTo(transactionDescriptor);
        }

        assertThat(transactionLockTypes).containsExactlyInAnyOrder(LockType.READ, LockType.WRITE);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockRowThenReadLockTableSameTransaction() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor, statementId)).isTrue();

        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor, statementId, LockType.READ);
        checkOneLockedTable(lockTable, tableId, transactionDescriptor, statementId, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockRowThenWriteLockTableSameTransaction() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockTable.tryWriteLockTable(tableId, transactionDescriptor, statementId)).isTrue();

        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor, statementId, LockType.READ);
        checkOneLockedTable(lockTable, tableId, transactionDescriptor, statementId, LockType.WRITE);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockRowThenReadLockTableSameTransaction() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryWriteLockRow(tableId, rowId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor, statementId)).isTrue();

        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor, statementId, LockType.WRITE);
        checkOneLockedTable(lockTable, tableId, transactionDescriptor, statementId, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockRowThenWriteLockTableSameTransaction() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryWriteLockRow(tableId, rowId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockTable.tryWriteLockTable(tableId, transactionDescriptor, statementId)).isTrue();

        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor, statementId, LockType.WRITE);
        checkOneLockedTable(lockTable, tableId, transactionDescriptor, statementId, LockType.WRITE);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockTableThenReadLockRowSameTransaction() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor, statementId)).isTrue();

        checkOneLockedTable(lockTable, tableId, transactionDescriptor, statementId, LockType.READ);
        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor, statementId, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockTableThenWriteLockRowSameTransaction() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockTable.tryWriteLockRow(tableId, rowId, transactionDescriptor, statementId)).isTrue();

        checkOneLockedTable(lockTable, tableId, transactionDescriptor, statementId, LockType.READ);
        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor, statementId, LockType.WRITE);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockTableThenReadLockRowSameTransaction() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryWriteLockTable(tableId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor, statementId)).isTrue();

        checkOneLockedTable(lockTable, tableId, transactionDescriptor, statementId, LockType.WRITE);
        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor, statementId, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockTableThenWriteLockRowSameTransaction() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor = 345;
        final int statementId = 456;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryWriteLockTable(tableId, transactionDescriptor, statementId)).isTrue();
        assertThat(lockTable.tryWriteLockRow(tableId, rowId, transactionDescriptor, statementId)).isTrue();

        checkOneLockedTable(lockTable, tableId, transactionDescriptor, statementId, LockType.WRITE);
        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor, statementId, LockType.WRITE);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockRowThenReadLockTableDifferentTransactions() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor2, statementId)).isTrue();

        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor1, statementId, LockType.READ);
        checkOneLockedTable(lockTable, tableId, transactionDescriptor2, statementId, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockRowThenWriteLockTableDifferentTransactions() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryWriteLockTable(tableId, transactionDescriptor2, statementId)).isFalse();

        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor1, statementId, LockType.READ);
        checkNotLockedTable(lockTable, tableId);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockRowThenReadLockTableDifferentTransactions() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryWriteLockRow(tableId, rowId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor2, statementId)).isFalse();

        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor1, statementId, LockType.WRITE);
        checkNotLockedTable(lockTable, tableId);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockRowThenWriteLockTableDifferentTransactions() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryWriteLockRow(tableId, rowId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryWriteLockTable(tableId, transactionDescriptor2, statementId)).isFalse();

        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor1, statementId, LockType.WRITE);
        checkNotLockedTable(lockTable, tableId);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockTableThenReadLockRowDifferentTransactions() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor2, statementId)).isTrue();

        checkOneLockedTable(lockTable, tableId, transactionDescriptor1, statementId, LockType.READ);
        checkOneLockedRow(lockTable, tableId, rowId, transactionDescriptor2, statementId, LockType.READ);
    }

    @Test
    @Category(UnitTest.class)
    public void testReadLockTableThenWriteLockRowDifferentTransactions() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryReadLockTable(tableId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryWriteLockRow(tableId, rowId, transactionDescriptor2, statementId)).isFalse();

        checkOneLockedTable(lockTable, tableId, transactionDescriptor1, statementId, LockType.READ);
        checkNotLockedRows(lockTable, tableId, rowId);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockTableThenReadLockRowDifferentTransactions() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryWriteLockTable(tableId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryReadLockRow(tableId, rowId, transactionDescriptor2, statementId)).isFalse();

        checkOneLockedTable(lockTable, tableId, transactionDescriptor1, statementId, LockType.WRITE);
        checkNotLockedRows(lockTable, tableId, rowId);
    }

    @Test
    @Category(UnitTest.class)
    public void testWriteLockTableThenWriteLockRowDifferentTransactions() {

        final int tableId = 123;
        final long rowId = 234L;
        final int transactionDescriptor1 = 345;
        final int transactionDescriptor2 = 456;
        final int statementId = 567;

        final LockTable lockTable = new LockTable(tableId + 1);

        assertThat(lockTable.tryWriteLockTable(tableId, transactionDescriptor1, statementId)).isTrue();
        assertThat(lockTable.tryWriteLockRow(tableId, rowId, transactionDescriptor2, statementId)).isFalse();

        checkOneLockedTable(lockTable, tableId, transactionDescriptor1, statementId, LockType.WRITE);
        checkNotLockedRows(lockTable, tableId, rowId);
    }

    private static void checkOneLockedTable(LockTable lockTable, int tableId, int transactionDescriptor, int statementId, LockType expectedLockType) {

        checkLockedTable(lockTable, tableId, expectedLockType == LockType.READ ? 1 : 0, expectedLockType == LockType.WRITE ? 1 : 0);

        final LockHolders lockHolders = lockTable.getTableLockHolders(tableId);

        checkOneLockHolder(lockHolders, transactionDescriptor, statementId, expectedLockType);
    }

    private static void checkLockedTable(LockTable lockTable, int tableId, int expectedNumReadLocks, int expectedNumWriteLocks) {

        final LockedTables lockedTables = lockTable.getLockedTables();

        assertThat(lockedTables).isNotNull();
        assertThat(lockedTables).isNotEmpty();
        assertThat(lockedTables).hasNumElements(tableId + 1);
        assertThat(lockedTables.getNumLocks(tableId, LockType.READ)).isEqualTo(expectedNumReadLocks);
        assertThat(lockedTables.getNumLocks(tableId, LockType.WRITE)).isEqualTo(expectedNumWriteLocks);
    }

    private static void checkNotLockedTable(LockTable lockTable, int tableId) {

        checkLockedTable(lockTable, tableId, 0, 0);

        assertThat(lockTable.getTableLockHolders(tableId)).isNull();
    }

    private static void checkOneLockedRow(LockTable lockTable, int tableId, long rowId, int transactionDescriptor, int statementId, LockType expectedLockType) {

        checkLockedRow(lockTable, tableId, rowId, expectedLockType == LockType.READ ? 1 : 0, expectedLockType == LockType.WRITE ? 1 : 0);

        final LockHolders lockHolders = lockTable.getRowLockHolders(tableId, rowId);

        checkOneLockHolder(lockHolders, transactionDescriptor, statementId, expectedLockType);
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

    private static void checkNotLockedRows(LockTable lockTable, int tableId, long rowId) {

        final LockedRows lockedRows = lockTable.getLockedRows();

        assertThat(lockedRows).isNotNull();
        assertThat(lockedRows).isEmpty();
        assertThat(lockedRows).hasNumElements(0L);

        assertThat(lockTable.getRowLockHolders(tableId, rowId)).isNull();
    }

    private static void checkOneLockHolder(LockHolders lockHolders, int transactionDescriptor, int statementId, LockType expectedLockType) {

        assertThat(lockHolders).isNotNull();
        assertThat(lockHolders.getNumElements()).isEqualTo(1);
        assertThat(lockHolders.getTransactionDescriptor(0)).isEqualTo(transactionDescriptor);
        assertThat(lockHolders.getTransactionLockType(0)).isEqualTo(expectedLockType);

        assertThat(lockHolders.getStatementId(0)).isEqualTo(statementId);
    }
}
