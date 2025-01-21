package dev.jdata.db;

public class DebugConstants {

    private static final boolean DEBUG_SESSION_MANAGEMENT = Boolean.FALSE;

    public static final boolean DEBUG_BASE_SESSION = DEBUG_SESSION_MANAGEMENT;

    private static final boolean DEBUG_TRANSACTION_MANAGEMENT = Boolean.FALSE;

    public static final boolean DEBUG_TRANSACTION = DEBUG_TRANSACTION_MANAGEMENT;
    public static final boolean DEBUG_TRANSACTION_LOCKING = DEBUG_TRANSACTION_MANAGEMENT;

    private static final boolean DEBUG_LOCKING = Boolean.TRUE;

    public static final boolean DEBUG_LOCK_TABLE = DEBUG_LOCKING;
    public static final boolean DEBUG_LOCK_TABLE_ROWS = DEBUG_LOCKING;

    private static final boolean DEBUG_ARRAYS = Boolean.FALSE;
    private static final boolean DEBUG_HASHED = Boolean.FALSE;
    private static final boolean DEBUG_MAPS = Boolean.FALSE;
    private static final boolean DEBUG_SETS = Boolean.FALSE;

    public static final boolean DEBUG_TWO_DIMENSIONAL_ARRAY = DEBUG_ARRAYS;

    public static final boolean DEBUG_BASE_HASHED = DEBUG_HASHED;
    public static final boolean DEBUG_BASE_EXPONENT_HASHED = DEBUG_HASHED;

    public static final boolean DEBUG_BASE_INT_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_INT_ARRAY_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_INT_TO_OBJECT_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_INT_TO_INT_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LONG_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_LONG_ARRAY_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_LONG_TO_OBJECT_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_LONG_TO_LONG_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_LONG_TO_INT_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_INT_SET = DEBUG_SETS;
    public static final boolean DEBUG_LONG_SET = DEBUG_SETS;
}
