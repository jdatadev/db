package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.DBBitEncoding;
import dev.jdata.db.DebugConstants;
import dev.jdata.db.LockType;
import dev.jdata.db.data.locktable.LockTable;
import dev.jdata.db.data.locktable.LockTable.NotLockedException;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.arrays.IMutableIntLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.elements.ILongByIndexOrderedElementsView;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

final class TransactionLocking extends TransactionMechanism<LockTable> {

    private static final boolean DEBUG = DebugConstants.DEBUG_TRANSACTION_LOCKING;

    private static final boolean ASSERT = AssertionContants.ASSERT_TRANSACTION_LOCKING;

    private final IMutableLongLargeArray lockedTableRows;
    private final IMutableIntLargeArray lockStatements;

    private int transactionDescriptor;

    TransactionLocking(int transactionDescriptor) {

        Checks.isTransactionDescriptor(transactionDescriptor);

        this.lockedTableRows = IMutableLongLargeArray.create();
        this.lockStatements = IMutableIntLargeArray.create();

        init(transactionDescriptor);
    }

    final void init(int transactionDescriptor) {

        this.transactionDescriptor = Checks.isTransactionDescriptor(transactionDescriptor);
    }

    @Override
    public OperationResult insertRows(LockTable lockTable, Table table, int statementId, ILongByIndexOrderedElementsView rowIds, DMLInsertRows rows) {

        Objects.requireNonNull(lockTable);
        Objects.requireNonNull(table);
        Checks.isStatementId(statementId);
        Objects.requireNonNull(rowIds);
        Objects.requireNonNull(rows);

        throw new UnsupportedOperationException();
    }

    @Override
    public OperationResult updateRows(LockTable lockTable, Table table, int statementId, ILongByIndexOrderedElementsView rowIds, DMLUpdateRows rows) {

        checkParameters(lockTable, table, statementId, rowIds, rows);

        final boolean locksAquired = lockTable.tryLockRows(table.getId(), rowIds, transactionDescriptor, statementId, LockType.WRITE);

        return locksAquired ? OperationResult.SUCCESS : OperationResult.LOCK_RETRY;
    }

    @Override
    public OperationResult updateAllRows(LockTable lockTable, Table table, int statementId, DMLUpdateRows row) {

        checkParameters(lockTable, table, statementId, row);

        return lockTable(lockTable, table, statementId);
    }

    @Override
    public OperationResult deleteRows(LockTable lockTable, Table table, int statementId, ILongByIndexOrderedElementsView rowIds) {

        checkParameters(lockTable, table, statementId, rowIds);

        final boolean locksAquired = lockTable.tryLockRows(table.getId(), rowIds, transactionDescriptor, statementId, LockType.WRITE);

        return locksAquired ? OperationResult.SUCCESS : OperationResult.LOCK_RETRY;
    }

    @Override
    public OperationResult deleteAllRows(LockTable lockTable, Table table, int statementId) {

        checkParameters(lockTable, table, statementId);

        return lockTable(lockTable, table, statementId);
    }

    private OperationResult lockTable(LockTable lockTable, Table table, int statementId) {

        final boolean locksAquired = lockTable.tryLockTable(table.getId(), transactionDescriptor, statementId, LockType.WRITE);

        return locksAquired ? OperationResult.SUCCESS : OperationResult.LOCK_RETRY;
    }

    @Override
    public final void commit(LockTable lockTable) {

        releaseLocks(lockTable);
    }

    @Override
    public final void rollback(LockTable lockTable) {

        releaseLocks(lockTable);
    }

    private synchronized void releaseLocks(LockTable lockTable) {

        if (ASSERT) {

            Assertions.isSameLimit(lockedTableRows, lockStatements);
        }

        Objects.requireNonNull(lockTable);

        final long numLockedRows = lockedTableRows.getLimit();

        for (long i = 0L; i < numLockedRows; ++ i) {

            final long encodedRowLock = lockedTableRows.get(i);

            final int tableId = DBBitEncoding.decodeLockTableId(encodedRowLock);
            final long rowId = DBBitEncoding.decodeLockRowId(encodedRowLock);
            final LockType lockType = DBBitEncoding.decodeLockType(encodedRowLock);

            final int statementId = lockStatements.get(i);

            try {
                lockTable.unlockRow(tableId, rowId, transactionDescriptor, statementId, lockType);
            }
            catch (NotLockedException ex) {

                throw new IllegalStateException();
            }
        }

        lockedTableRows.clear();
        lockStatements.clear();
    }
}
