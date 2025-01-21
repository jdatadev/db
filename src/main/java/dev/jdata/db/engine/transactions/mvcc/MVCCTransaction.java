package dev.jdata.db.engine.transactions.mvcc;

import java.util.Objects;

import dev.jdata.db.data.RowDataNumBitsGetter;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.engine.transactions.TransactionMechanism;
import dev.jdata.db.schema.Table;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.maps.LongToLongMap;
import dev.jdata.db.utils.checks.Checks;
final class MVCCTransaction extends TransactionMechanism<Object> {

    private final long transactionId;

    private final LongToLongMap indexByRow;

    private byte[][] insertRows;
    private byte[][] updateRows;

    MVCCTransaction(long transactionId) {

        this.transactionId = Checks.isTransactionId(transactionId);

        this.indexByRow = new LongToLongMap(0);
    }

    @Override
    public OperationResult insertRows(Object sharedState, Table table, int statementId, DMLInsertRows rows) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OperationResult updateRows(Object sharedState, Table table, int statementId, LargeLongArray rowIds,
            DMLUpdateRows rows) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OperationResult updateAllRows(Object sharedState, Table table, int statementId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OperationResult deleteRows(Object sharedState, Table table, int statementId, LargeLongArray rowIds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OperationResult deleteAllRows(Object sharedState, Table table, int statementId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void commit(Object sharedState) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollback(Object sharedState) {
        // TODO Auto-generated method stub

    }

    private void insertRow(int tableId, long rowId, byte[] outputBuffer, long outputBufferStartBitOffset, int[] columnIndices, RowDataNumBitsGetter outputRowDataNumBitsGetter) {

//        checkParameters(tableId, rowId, columnIndices, outputRowDataNumBitsGetter);
        Objects.requireNonNull(columnIndices);

        final long key = makeHashKey(tableId, rowId);

        indexByRow.put(key, key);
    }

    void updateRow(int tableId, long rowId, byte[] outputBuffer, long outputBufferStartBitOffset, int[] columnIndices, RowDataNumBitsGetter outputRowDataNumBitsGetter) {

//        checkParameters(tableId, rowId, columnIndices, outputRowDataNumBitsGetter);
        Objects.requireNonNull(columnIndices);

        final long key = makeHashKey(tableId, rowId);

        indexByRow.put(key, key);
    }

    private void deleteRow(int tableId, long rowId) {

        Checks.isTableId(tableId);
        Checks.isRowId(rowId);

    }
}
