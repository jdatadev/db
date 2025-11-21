package dev.jdata.db;

public class DBConstants {

    public static final int INITIAL_DESCRIPTORABLE = 0;

    public static final int NO_SCHEMA_OBJECT_ID = -1;
    public static final int INITIAL_SCHEMA_OBJECT_ID = 0;

    public static final int NO_TABLE_ID = NO_SCHEMA_OBJECT_ID;

    public static final int INITIAL_COLUMN_ID = INITIAL_SCHEMA_OBJECT_ID;

    public static final int FILE_NO_SEQUENCE_NO = -1;
    public static final int FILE_INITIAL_SEQUENCE_NO = 0;

    public static final long NO_TRANSACTION_ID = -1L;
    public static final long INITIAL_TRANSACTION_ID = 0L;

    public static final long NO_ROW_ID = -1L;
    public static final long INITIAL_ROW_ID = 0L;

    public static final int MAX_COLUMNS = 1 << 16;
    public static final int MAX_COLUMN_INDEX = MAX_COLUMNS - 1;

    public static final int MAX_BYTES_PER_COLUMN = 16;
    public static final int MAX_BITS_PER_COLUMN = MAX_BYTES_PER_COLUMN * Byte.SIZE;

    @Deprecated
    public static final int MAX_BYTES_PER_ROW = MAX_BYTES_PER_COLUMN * MAX_COLUMNS;

    public static final int TRANSACTION_ID_NUM_BITS = Long.SIZE;
    public static final int TRANSACTION_DESCRIPTOR_NUM_BITS = Integer.SIZE;
    public static final int STATEMENT_ID_NUM_BITS = Integer.SIZE;

    public static final int MAX_DB_NAME_LENGTH = 1 << 8;
    public static final int MAX_CHARSET_NAME_LENGTH = 1 << 8;

    public static final int MAX_DECIMAL_PRECISION = 30;
}
