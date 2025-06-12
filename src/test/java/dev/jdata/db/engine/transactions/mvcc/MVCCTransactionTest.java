package dev.jdata.db.engine.transactions.mvcc;

import java.nio.ByteBuffer;
import java.util.function.IntFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.DBConstants;
import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.dml.DMLInsertUpdateRows;
import dev.jdata.db.dml.DMLInsertUpdateRows.InsertUpdateRow;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.dml.DMLUpdateRows.UpdateRow;
import dev.jdata.db.engine.database.StringStorer;
import dev.jdata.db.engine.transactions.RowValue;
import dev.jdata.db.engine.transactions.SelectColumn;
import dev.jdata.db.engine.transactions.StringLookup;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.engine.transactions.mvcc.MVCCTransaction.MVCCTransactionState;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.test.unit.BaseDBTest;
import dev.jdata.db.test.unit.TableBuilder;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.arrays.ObjectArray;
import dev.jdata.db.utils.adt.sets.MutableLongBucketSet;

public final class MVCCTransactionTest extends BaseDBTest {

    @Test
    @Category(UnitTest.class)
    public void testInsert() {

        for (int i = 3; i <= 20; ++ i) {

            checkInsert(i);
        }
    }

    private void checkInsert(int innerCapacityExponent) {

        final MVCCTransaction mvccTransaction = new MVCCTransaction(innerCapacityExponent);

        final int transactionDescriptor = 0;
        final long originatingTransactionId = DBConstants.NO_TRANSACTION_ID;
        final DBIsolationLevel isolationLevel = DBIsolationLevel.COMMITTED_READ;

        final int tableId = 123;

        final int numTables = tableId + 1;

        mvccTransaction.prepare(numTables);

        mvccTransaction.initialize(transactionDescriptor, originatingTransactionId, isolationLevel);

        final int statementId = 234;
        final long rowId = 345L;
        final int intValue = 56;

        final Table table = createTestTable(tableId);

        final MVCCTransactionState mvccSharedState = new MVCCTransactionState();

        final LargeLongArray rowIds = new LargeLongArray(1, 10, null);

        final DMLInsertRows rows = makeInsertRows(rowId, rowIds, intValue);

        mvccTransaction.insertRows(mvccSharedState, table, statementId, rowIds, rows);

        final MutableLongBucketSet addedRowIds = new MutableLongBucketSet();
        final MutableLongBucketSet removedRowIds = new MutableLongBucketSet();

        final TransactionSelect select = makeTransactionSelect(tableId, rowId, intValue);

        mvccTransaction.select(select, null, addedRowIds, removedRowIds);

        assertThat(addedRowIds).containsExactlyInAnyOrder(rowId);
        assertThat(removedRowIds).isEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public void testUpdate() {

        for (int i = 3; i <= 20; ++ i) {

            checkUpdate(i);
        }
    }

    private static final class BufferedRowsImpl implements BufferedRows {

        @Override
        public boolean compareColumn(int tableId, long rowId, SelectColumn selectColumn, int tableColumn) {

            throw new UnsupportedOperationException();
        }
    }

    private void checkUpdate(int innerCapacityExponent) {

        final MVCCTransaction mvccTransaction = new MVCCTransaction(innerCapacityExponent);

        final int transactionDescriptor = 0;
        final long originatingTransactionId = DBConstants.NO_TRANSACTION_ID;
        final DBIsolationLevel isolationLevel = DBIsolationLevel.COMMITTED_READ;

        final int tableId = 123;

        final int numTables = tableId + 1;

        mvccTransaction.prepare(numTables);

        mvccTransaction.initialize(transactionDescriptor, originatingTransactionId, isolationLevel);

        final int statementId = 234;
        final long rowId = 345L;
        final int intValue = 56;

        final Table table = createTestTable(tableId);

        final MVCCTransactionState mvccSharedState = new MVCCTransactionState();

        final LargeLongArray rowIds = new LargeLongArray(1, 10, null);

        final DMLUpdateRows rows = makeUpdateRows(rowId, rowIds, intValue);

        mvccTransaction.updateRows(mvccSharedState, table, statementId, rowIds, rows);

        final MutableLongBucketSet addedRowIds = new MutableLongBucketSet();
        final MutableLongBucketSet removedRowIds = new MutableLongBucketSet();

        final TransactionSelect select = makeTransactionSelect(tableId, rowId, intValue);

        mvccTransaction.select(select, new BufferedRowsImpl(), addedRowIds, removedRowIds);

        assertThat(addedRowIds).containsExactlyInAnyOrder(rowId);
        assertThat(removedRowIds).isEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public void testUpdateAll() {

        for (int i = 3; i <= 20; ++ i) {

            checkUpdateAll(i);
        }
    }

    private void checkUpdateAll(int innerCapacityExponent) {

        throw new UnsupportedOperationException();
/*
        final MVCCTransaction mvccTransaction = new MVCCTransaction(innerCapacityExponent);

        final int transactionDescriptor = 0;
        final long originatingTransactionId = DBConstants.NO_TRANSACTION_ID;
        final DBIsolationLevel isolationLevel = DBIsolationLevel.COMMITTED_READ;

        mvccTransaction.initialize(transactionDescriptor, originatingTransactionId, isolationLevel);

        final String tableName = "tableName";
        final int tableId = 123;
        final int statementId = 234;
        final long rowId = 345L;
        final int intValue = 56;

        final Column tableColumn = new Column(IntegerType.INSTANCE, false);
        final Table table = new Table(tableName, tableId, Arrays.asList(tableColumn));

        final MVCCTransactionState mvccSharedState = new MVCCTransactionState();

        final DMLUpdateRows rows = makeUpdateAllRows(intValue);

        mvccTransaction.updateAllRows(mvccSharedState, table, statementId, rows);

        final LongSet addedRowIds = new LongSet();
        final LongSet removedRowIds = new LongSet();

        final TransactionSelect select = makeTransactionSelect(tableId, rowId, intValue);

        mvccTransaction.select(select, addedRowIds, removedRowIds);

        assertThat(addedRowIds).containsExactlyInAnyOrder(rowId);
        assertThat(removedRowIds).isEmpty();
*/
    }

    @Test
    @Category(UnitTest.class)
    public void testDelete() {

        for (int i = 3; i <= 20; ++ i) {

            checkDelete(i);
        }
    }

    private void checkDelete(int innerCapacityExponent) {

        final MVCCTransaction mvccTransaction = new MVCCTransaction(innerCapacityExponent);

        final int transactionDescriptor = 0;
        final long originatingTransactionId = DBConstants.NO_TRANSACTION_ID;
        final DBIsolationLevel isolationLevel = DBIsolationLevel.COMMITTED_READ;

        final int tableId = 123;

        final int numTables = tableId + 1;

        mvccTransaction.prepare(numTables);

        mvccTransaction.initialize(transactionDescriptor, originatingTransactionId, isolationLevel);

        final int statementId = 234;
        final long rowId = 345L;
        final int intValue = 56;

        final Table table = createTestTable(tableId);

        final MVCCTransactionState mvccSharedState = new MVCCTransactionState();

        final LargeLongArray rowIds = new LargeLongArray(1, 10, null);

        rowIds.add(rowId);

        mvccTransaction.deleteRows(mvccSharedState, table, statementId, rowIds);

        final MutableLongBucketSet addedRowIds = new MutableLongBucketSet();
        final MutableLongBucketSet removedRowIds = new MutableLongBucketSet();

        final TransactionSelect select = makeTransactionSelect(tableId, rowId, intValue);

        mvccTransaction.select(select, null, addedRowIds, removedRowIds);

        assertThat(addedRowIds).isEmpty();
        assertThat(removedRowIds).containsExactlyInAnyOrder(rowId);
    }

    @Test
    @Category(UnitTest.class)
    public void testInsertThenDelete() {

        for (int i = 3; i <= 20; ++ i) {

            checkInsertThenDelete(i);
        }
    }

    private void checkInsertThenDelete(int innerCapacityExponent) {

        final MVCCTransaction mvccTransaction = new MVCCTransaction(innerCapacityExponent);

        final int transactionDescriptor = 0;
        final long originatingTransactionId = DBConstants.NO_TRANSACTION_ID;
        final DBIsolationLevel isolationLevel = DBIsolationLevel.COMMITTED_READ;

        final int tableId = 123;

        final int numTables = tableId + 1;

        mvccTransaction.prepare(numTables);

        mvccTransaction.initialize(transactionDescriptor, originatingTransactionId, isolationLevel);

        final int statementId = 234;
        final long rowId = 345L;
        final int intValue = 56;

        final Table table = createTestTable(tableId);

        final MVCCTransactionState mvccSharedState = new MVCCTransactionState();

        final LargeLongArray rowIds = new LargeLongArray(1, 10, null);

        final DMLInsertRows rows = makeInsertRows(rowId, rowIds, intValue);

        mvccTransaction.insertRows(mvccSharedState, table, statementId, rowIds, rows);

        mvccTransaction.deleteRows(mvccSharedState, table, statementId, rowIds);

        final MutableLongBucketSet addedRowIds = new MutableLongBucketSet();
        final MutableLongBucketSet removedRowIds = new MutableLongBucketSet();

        final TransactionSelect select = makeTransactionSelect(tableId, rowId, intValue);

        mvccTransaction.select(select, null, addedRowIds, removedRowIds);

        assertThat(addedRowIds).isEmpty();
        assertThat(removedRowIds).containsExactlyInAnyOrder(rowId);
    }

    @Test
    @Category(UnitTest.class)
    public void testDeleteAll() {

        for (int i = 3; i <= 20; ++ i) {

            checkDeleteAll(i);
        }
    }

    private void checkDeleteAll(int innerCapacityExponent) {

        throw new UnsupportedOperationException();

/*
        final MVCCTransaction mvccTransaction = new MVCCTransaction(innerCapacityExponent);

        final int transactionDescriptor = 0;
        final long originatingTransactionId = DBConstants.NO_TRANSACTION_ID;
        final DBIsolationLevel isolationLevel = DBIsolationLevel.COMMITTED_READ;

        final int tableId = 123;

        final int numTables = tableId + 1;

        mvccTransaction.prepare(numTables);

        mvccTransaction.initialize(transactionDescriptor, originatingTransactionId, isolationLevel);

        final String tableName = "tableName";
        final int statementId = 234;
        final long rowId = 345L;
        final int intValue = 56;

        final Column tableColumn = new Column(IntegerType.INSTANCE, false);
        final Table table = new Table(tableName, tableId, Arrays.asList(tableColumn));

        final MVCCTransactionState mvccSharedState = new MVCCTransactionState();

        final LongLargeArray rowIds = new LongLargeArray(1, 10);

//        final DMLInsertRows rows = makeInsertRows(rowId, rowIds, intValue);

        mvccTransaction.deleteRows(mvccSharedState, table, statementId, rowIds);

        final LongSet addedRowIds = new LongSet();
        final LongSet removedRowIds = new LongSet();

        final TransactionSelect select = makeTransactionSelect(tableId, rowId, intValue);

        mvccTransaction.select(select, null, addedRowIds, removedRowIds);

        assertThat(addedRowIds).isEmpty();
        assertThat(removedRowIds).containsExactlyInAnyOrder(rowId);
*/
    }

    private static DMLInsertRows makeInsertRows(long rowId, LargeLongArray rowIds, int intValue) {

        return makeInsertUpdateRows(rowId, rowIds, new DMLInsertRows(), new InsertRow(), intValue, InsertRow[]::new);
    }

    private static DMLUpdateRows makeUpdateRows(long rowId, LargeLongArray rowIds, int intValue) {

        return makeInsertUpdateRows(rowId, rowIds, new DMLUpdateRows(), new UpdateRow(), intValue, UpdateRow[]::new);
    }

    private static DMLUpdateRows makeUpdateAllRows(int intValue) {

        return makeInsertUpdateRows(new DMLUpdateRows(), new UpdateRow(), intValue, UpdateRow[]::new);
    }

    private static <T extends InsertUpdateRow, U extends DMLInsertUpdateRows<T>> U makeInsertUpdateRows(long rowId, LargeLongArray rowIds, U rows, T row, int intValue,
            IntFunction<T[]> createRowArray) {

        rowIds.add(rowId);

        return makeInsertUpdateRows(rows, row, intValue, createRowArray);
    }

    private static <T extends InsertUpdateRow, U extends DMLInsertUpdateRows<T>> U makeInsertUpdateRows(U rows, T row, int intValue, IntFunction<T[]> createRowArray) {

        final int numColumns = 1;

        final RowDataNumBits rowDataNumBits = new RowDataNumBits(numColumns);

        rowDataNumBits.addNumBits(Integer.SIZE);

        final byte[] rowBuffer = new byte[] { 0, 0, 0, (byte)intValue };

        row.initialize(ByteBuffer.wrap(rowBuffer), 0L);

        final T[] rowArray = createRowArray.apply(1);

        rowArray[0] = row;

        final int numRows = rowArray.length;

        rows.initialize(rowArray, numRows, numColumns, rowDataNumBits);

        return rows;
    }

    private static TransactionSelect makeTransactionSelect(int tableId, long rowId, int intValue) {

        final TransactionSelect select = new TransactionSelect();

        final SelectColumn selectColumn = new SelectColumn();

        final RowValue rowValue = new RowValue();

        rowValue.setInt(intValue);

        selectColumn.initialize(0, ComparisonOperator.EQUAL_TO, rowValue);

        final ObjectArray<SelectColumn> selectColumns = ObjectArray.of(selectColumn);

        final MutableLongBucketSet rowIdsToFilter = MutableLongBucketSet.of(rowId);

        final StringLookup stringLookup = new StringLookup() {

            @Override
            public int compare(long string1, long string2) {

                throw new UnsupportedOperationException();
            }
        };

        select.initialize(tableId, null, selectColumns, rowIdsToFilter, stringLookup);

        return select;
    }

    private static Table createTestTable(int tableId) {

        final String tableName = "tableName";

        final StringStorer stringStorer = createStringStorer();

        return TableBuilder.create(tableName, tableId, stringStorer)
                .addColumn("testcolumn", IntegerType.INSTANCE)
                .build();
    }
}
