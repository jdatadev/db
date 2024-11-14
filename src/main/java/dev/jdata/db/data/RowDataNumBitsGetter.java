package dev.jdata.db.data;

public interface RowDataNumBitsGetter {

    int getNumColumns();

    boolean isNull(int columnIndex);

    int getNumBits(int columnIndex);

    default int getTotalNumRowDataBits() {

        final int numColumns = getNumColumns();

        int totalNumRowDataBits = 0;

        for (int i = 0; i < numColumns; ++ i) {

            totalNumRowDataBits += getNumBits(i);
        }

        return totalNumRowDataBits;
    }
}
