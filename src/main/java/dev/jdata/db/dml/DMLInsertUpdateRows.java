package dev.jdata.db.dml;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.checks.Checks;

public abstract class DMLInsertUpdateRows<T extends DMLInsertUpdateRows.InsertUpdateRow> extends DMLRows<T> {

    public static abstract class InsertUpdateRow extends DMLRow {

        private ByteBuffer rowBuffer;
        private long rowBufferBitOffset;

        public final void initialize(ByteBuffer rowBuffer, long rowBufferBitOffset) {

            this.rowBuffer = Objects.requireNonNull(rowBuffer);
            this.rowBufferBitOffset = Checks.isOffset(rowBufferBitOffset);
        }

        final ByteBuffer getRowBuffer() {
            return rowBuffer;
        }

        final long getRowBufferBitOffset() {
            return rowBufferBitOffset;
        }

        @Override
        public String toString() {

            return getClass().getSimpleName() + " [rowBuffer=" + rowBuffer + ", rowBufferBitOffset=" + rowBufferBitOffset + "]";
        }
    }

    private final int[] tableColumns;
    private RowDataNumBits rowDataNumBits;

    private int numColumns;

    DMLInsertUpdateRows() {

        this.tableColumns = new int[DBConstants.MAX_COLUMNS];
    }

    public final void initialize(T[] rows, int numRows, int numColumns, RowDataNumBits rowDataNumBits) {

        Checks.checkNumElements(rows, numRows);
        Checks.isNumColumns(numColumns);
        Objects.requireNonNull(rowDataNumBits);
        Checks.areEqual(numColumns, rowDataNumBits.getNumColumns());

        initialize(rows, numRows);

        this.numColumns = numColumns;
        this.rowDataNumBits = rowDataNumBits;
    }

    public final void setColumnMapping(int index, int tableColumn) {

        Objects.checkIndex(index, numColumns);
        Checks.isColumnIndex(tableColumn);

        tableColumns[index] = tableColumn;
    }

    public final RowDataNumBits getRowDataNumBits() {

        return rowDataNumBits;
    }

    public final int getNumTableColumns() {
        return numColumns;
    }

    public final int getTableColumn(int index) {

        Objects.checkIndex(index, numColumns);

        return tableColumns[index];
    }

    public final ByteBuffer getRowBuffer(int rowIndex) {

        return getRows()[rowIndex].getRowBuffer();
    }

    public final long getRowBufferBitOffset(int rowIndex) {

        return getRows()[rowIndex].getRowBufferBitOffset();
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [tableColumns=" + Array.toString(tableColumns, 0, numColumns) + ", rowDataNumBits=" + rowDataNumBits +
                ", numColumns=" + numColumns + ", getRows()=" + Arrays.toString(getRows()) + "]";
    }
}
