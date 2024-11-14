package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.data.BaseRows;
import dev.jdata.db.data.RowDataNumBitsGetter;
import dev.jdata.db.utils.adt.maps.LongToLongMap;
import dev.jdata.db.utils.checks.Checks;

final class MVCCTransaction extends BaseRows {

    private final long transactionId;

    private final LongToLongMap indexByRow;

    private byte[][] insertRows;
    private byte[][] updateRows;

    MVCCTransaction(long transactionId) {

        this.transactionId = Checks.isTransactionId(transactionId);

        this.indexByRow = new LongToLongMap(0, long[]::new);
    }

    void insertRow(int tableId, long rowId, byte[] outputBuffer, long outputBufferStartBitOffset, int[] columnIndices, RowDataNumBitsGetter outputRowDataNumBitsGetter) {

        checkParameters(tableId, rowId, columnIndices, outputRowDataNumBitsGetter);
        Objects.requireNonNull(columnIndices);

        final long key = makeKey(tableId, rowId);

        indexByRow.put(key, key);
    }

    void updateRow(int tableId, long rowId, byte[] outputBuffer, long outputBufferStartBitOffset, int[] columnIndices, RowDataNumBitsGetter outputRowDataNumBitsGetter) {

        checkParameters(tableId, rowId, columnIndices, outputRowDataNumBitsGetter);
        Objects.requireNonNull(columnIndices);

        final long key = makeKey(tableId, rowId);

        indexByRow.put(key, key);
    }

    void deleteRow(int tableId, long rowId) {

        Checks.isTableId(tableId);
        Checks.isRowId(rowId);

    }
}
