package dev.jdata.db.data;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.ToStringable;

public class RowDataNumBits extends ToStringable implements RowDataNumBitsGetter, IContainsView, IClearable {

    public interface IRowDataNumBitsAllocator {

        RowDataNumBits allocateRowDataNumBits();

        void freeRowDataNumBits(RowDataNumBits rowDataNumBits);
    }

    private final int[] rowDataNumBits;

    private int numColumns;

    private int totalNumRowDataBits;

    public RowDataNumBits(int capacity) {

        Checks.isNumColumns(capacity);

        this.rowDataNumBits = new int[capacity];
    }

    @Override
    public final boolean isEmpty() {

        return numColumns == 0;
    }

    @Override
    public final int getNumColumns() {

        return numColumns;
    }

    @Override
    public final boolean isNull(int columnIndex) {

        Checks.checkIntIndex(columnIndex, numColumns);

        return rowDataNumBits[columnIndex] == 0;
    }

    @Override
    public final int getNumBits(int columnIndex) {

        Checks.checkIntIndex(columnIndex, numColumns);

        return rowDataNumBits[columnIndex];
    }

    @Override
    public final int getTotalNumRowDataBits() {
        return totalNumRowDataBits;
    }

    public final int addNumBits(int numBits) {

        final int columnIndex = numColumns ++;

        setNumBits(columnIndex, numBits);

        return columnIndex;
    }

    private void setNumBits(int columnIndex, int numBits) {

        Checks.checkIntIndex(columnIndex, numColumns);
        Checks.isColumnIndex(columnIndex);
        Checks.isNumBits(numBits);

        if (rowDataNumBits[columnIndex] != 0) {

            throw new IllegalStateException();
        }

        rowDataNumBits[columnIndex] = numBits;

        this.totalNumRowDataBits += numBits;
    }

    @Override
    public void clear() {

        this.numColumns = 0;
        this.totalNumRowDataBits = 0;
    }
}