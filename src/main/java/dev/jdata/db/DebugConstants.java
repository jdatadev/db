package dev.jdata.db;

public class DebugConstants {

    private static final boolean DEBUG_SESSION_MANAGEMENT = Boolean.FALSE;

    public static final boolean DEBUG_DB_SESSION = DEBUG_SESSION_MANAGEMENT;

    private static final boolean DEBUG_TRANSACTION_MANAGEMENT = Boolean.FALSE;

    public static final boolean DEBUG_TRANSACTION = DEBUG_TRANSACTION_MANAGEMENT;
    public static final boolean DEBUG_TRANSACTION_LOCKING = DEBUG_TRANSACTION_MANAGEMENT;

    private static final boolean DEBUG_MVCC = Boolean.FALSE;

    public static final boolean DEBUG_MVCC_TRANSACTION = DEBUG_MVCC;
    public static final boolean DEBUG_MVCC_ROW_BUFFER_COMPARER = DEBUG_MVCC;

    private static final boolean DEBUG_LOCKING = Boolean.TRUE;

    public static final boolean DEBUG_LOCK_TABLE = DEBUG_LOCKING;
    public static final boolean DEBUG_LOCK_TABLE_ROWS = DEBUG_LOCKING;

    private static final boolean DEBUG_ARRAYS = Boolean.FALSE;
    private static final boolean DEBUG_HASHED = Boolean.FALSE;
    private static final boolean DEBUG_MAPS = Boolean.FALSE;
    private static final boolean DEBUG_SETS = Boolean.FALSE;

    public static final boolean DEBUG_BIT_BUFFER = Boolean.FALSE;

    public static final boolean DEBUG_TWO_DIMENSIONAL_ARRAY = DEBUG_ARRAYS;

    public static final boolean DEBUG_BASE_HASHED = DEBUG_HASHED;
    public static final boolean DEBUG_BASE_INT_CAPACITY_HASHED = DEBUG_HASHED;
    public static final boolean DEBUG_BASE_ARRAY_HASHED = DEBUG_HASHED;
    public static final boolean DEBUG_BASE_EXPONENT_HASHED = DEBUG_HASHED;

    public static final boolean DEBUG_BASE_INT_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_INT_ARRAY_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_INT_TO_OBJECT_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_INT_TO_LONG_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_INT_TO_INT_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LONG_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_LONG_ARRAY_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_LONG_TO_OBJECT_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_LONG_TO_LONG_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_LONG_TO_INT_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_OBJECT_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_OBJECT_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_INT_SET = DEBUG_SETS;
    public static final boolean DEBUG_LONG_SET = DEBUG_SETS;

    public static final boolean DEBUG_LARGE_LONG_SET = DEBUG_SETS;

    public static final boolean DEBUG_BIT_BUFFER_UTIL = Boolean.FALSE;
}
