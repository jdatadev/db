package dev.jdata.db.data;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

public final class RowDataNumBitsAndOffsets extends RowDataNumBits {

    private final int[] rowDataBitOffsets;

    public RowDataNumBitsAndOffsets(int numColumns) {
        super(numColumns);

        this.rowDataBitOffsets = new int[numColumns];
    }

    public final void addNumBitsAndOffset(int numBits, int bitOffset) {

        final int columnIndex = addNumBits(numBits);

        setNumBitsAndOffset(columnIndex, numBits, bitOffset);
    }

    private void setNumBitsAndOffset(int columnIndex, int numBits, int bitOffset) {

        rowDataBitOffsets[columnIndex] = bitOffset;
    }

    public int getRowDataBitOffset(int columnIndex) {

        Checks.checkIndex(columnIndex, getNumColumns());

        return rowDataBitOffsets[columnIndex];
    }

    @Override
    public void clear() {

        super.clear();

        Arrays.fill(rowDataBitOffsets, -1);
    }
}
