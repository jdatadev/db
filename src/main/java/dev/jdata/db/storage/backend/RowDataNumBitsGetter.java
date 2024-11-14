package dev.jdata.db.storage.backend;

public interface RowDataNumBitsGetter {

    boolean isNull(int columnIndex);

    int getNumBits(int columnIndex);
}
