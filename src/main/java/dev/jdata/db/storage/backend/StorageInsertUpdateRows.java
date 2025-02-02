package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.checks.Checks;

public abstract class StorageInsertUpdateRows<T extends StorageInsertUpdateRows.InsertUpdateRow> extends StorageDMLRows<T> {

    static abstract class InsertUpdateRow extends StorageDMLRow {

        private byte[] rowBuffer;
        private long rowBufferBitOffset;
        private RowDataNumBits rowDataNumBits;

        public final void initialize(DatabaseSchemaVersion databaseSchemaVersion, long rowId, long transactionId, byte[] rowBuffer, long rowBufferBitOffset,
                RowDataNumBits rowDataNumBits) {

            initializeDMLRow(databaseSchemaVersion, rowId, transactionId);

            this.rowBuffer = Objects.requireNonNull(rowBuffer);
            this.rowBufferBitOffset = Checks.isOffset(rowBufferBitOffset);
            this.rowDataNumBits = Objects.requireNonNull(rowDataNumBits);
        }

        final byte[] getRowBuffer() {
            return rowBuffer;
        }

        final long getRowBufferBitOffset() {
            return rowBufferBitOffset;
        }

        final RowDataNumBits getRowDataNumBits() {
            return rowDataNumBits;
        }
    }

    private int maxTotalNumRowBytes;

    @Override
    protected final void initialize(T[] rows, int numRows) {

        super.initialize(rows, numRows);

        Checks.atMost(rows, numRows);

        final int maxTotalNumRowBits = Array.max(rows, -1, r -> r.getRowDataNumBits().getTotalNumRowDataBits());

        this.maxTotalNumRowBytes = BitBufferUtil.numBytes(maxTotalNumRowBits);
    }

    public final byte[] getRowBuffer(int index) {

        return getRows()[index].getRowBuffer();
    }

    public final long getRowBufferBitOffset(int index) {

        return getRows()[index].getRowBufferBitOffset();
    }

    public final RowDataNumBits getRowDataNumBits(int index) {

        return getRows()[index].getRowDataNumBits();
    }

    public final int getMaxTotalNumRowBytes() {
        return maxTotalNumRowBytes;
    }
}
