package dev.jdata.db.dml;

import java.util.Objects;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.utils.checks.Checks;

public abstract class DMLInsertUpdateRows<T extends DMLInsertUpdateRows.InsertUpdateRow> extends DMLRows<T> {

    static abstract class InsertUpdateRow extends DMLRow {

        private byte[] rowBuffer;
        private long rowBufferBitOffset;
        private RowDataNumBits rowDataNumBits;

        final void initialize(byte[] rowBuffer, long rowBufferBitOffset, RowDataNumBits rowDataNumBits) {

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

    public final byte[] getRowBuffer(int index) {

        return getRows()[index].getRowBuffer();
    }

    public final long getRowBufferBitOffset(int index) {

        return getRows()[index].getRowBufferBitOffset();
    }

    public final RowDataNumBits getRowDataNumBits(int index) {

        return getRows()[index].getRowDataNumBits();
    }
}
