package dev.jdata.db.data;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

public final class RowDataNumBits implements RowDataNumBitsGetter {

    private final int[] rowDataNumBits;

    private int totalNumRowDataBits;

    RowDataNumBits(int numColumns) {

        Checks.isNumColumnBits(numColumns);

        this.rowDataNumBits = new int[numColumns];
    }

    @Override
    public int getNumColumns() {

        return rowDataNumBits.length;
    }

    @Override
    public boolean isNull(int columnIndex) {

        return rowDataNumBits[columnIndex] == 0;
    }

    @Override
    public int getNumBits(int columnIndex) {

        return rowDataNumBits[columnIndex];
    }

    @Override
    public int getTotalNumRowDataBits() {
        return totalNumRowDataBits;
    }

    public void setNumBits(int columnIndex, int numBits) {

        Checks.isColumnIndex(columnIndex);
        Checks.isNumBits(numBits);

        if (rowDataNumBits[columnIndex] != 0) {

            throw new IllegalStateException();
        }

        rowDataNumBits[columnIndex] = numBits;

        this.totalNumRowDataBits += numBits;
    }

    public void reset() {

        Arrays.fill(rowDataNumBits, 0);

        this.totalNumRowDataBits = 0;
    }
}