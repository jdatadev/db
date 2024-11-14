package dev.jdata.db.data;

import java.util.Objects;

public abstract class BaseRows extends BaseRowMap {

    protected static void checkParameters(int tableId, long rowId, int[] columnIndices, RowDataNumBitsGetter rowDataNumBitsGetter) {

        checkParameters(tableId, rowId);

        Objects.requireNonNull(columnIndices);
        Objects.requireNonNull(rowDataNumBitsGetter);
    }
}
