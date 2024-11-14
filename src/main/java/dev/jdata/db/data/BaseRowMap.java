package dev.jdata.db.data;

import dev.jdata.db.utils.checks.Checks;

public abstract class BaseRowMap {

    private static final int NUM_BITS = Long.SIZE - 1;

    private static final int TABLE_ID_BITS = 16;
    private static final int ROW_ID_BITS = NUM_BITS - TABLE_ID_BITS;

    private static final int MAX_TABLE_ID = 1 << TABLE_ID_BITS;
    private static final long MAX_ROW_ID = 1L << ROW_ID_BITS;

    protected static long makeKey(int tableId, long rowId) {

        return ((long)tableId) << ROW_ID_BITS | rowId;
    }

    protected static void checkParameters(int tableId, long rowId) {

        Checks.isTableId(tableId);

        if (tableId > MAX_TABLE_ID) {

            throw new IllegalArgumentException();
        }

        Checks.isRowId(rowId);

        if (rowId > MAX_ROW_ID) {

            throw new IllegalArgumentException();
        }
    }
}
