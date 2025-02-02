package dev.jdata.db;

public class DBConstants {

    public static final long NO_TRANSACTION_ID = -1L;
    public static final long NO_ROW_ID = -1L;

    public static final int MAX_COLUMNS = 1 << 16;
    public static final int MAX_COLUMN_INDEX = MAX_COLUMNS - 1;

    public static final int TRANSACTION_ID_NUM_BITS = Long.SIZE;
    public static final int TRANSACTION_DESCRIPTOR_NUM_BITS = Integer.SIZE;
    public static final int STATEMENT_ID_NUM_BITS = Integer.SIZE;
}
