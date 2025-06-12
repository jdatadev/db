package dev.jdata.db;

public class DebugConstants {

    public static final boolean DEBUG_STRING_STORER = Boolean.FALSE;

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
    public static final boolean DEBUG_LARGE_CHAR_ARRAY = DEBUG_ARRAYS;

    public static final boolean DEBUG_HASH_ARRAY = DEBUG_HASHED;
    public static final boolean DEBUG_MAX_DISTANCE = DEBUG_HASHED;

    public static final boolean DEBUG_BASE_HASHED = DEBUG_HASHED;
    public static final boolean DEBUG_BASE_INT_CAPACITY_HASHED = DEBUG_HASHED;
    public static final boolean DEBUG_BASE_ARRAY_HASHED = DEBUG_HASHED;
    public static final boolean DEBUG_BASE_EXPONENT_HASHED = DEBUG_HASHED;

    public static final boolean DEBUG_BASE_INT_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_INT_NON_BUCKET_ARRAY_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_INT_TO_OBJECT_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_INT_TO_OBJECT_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_INT_TO_INT_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_INT_TO_INT_MAX_DISTANCE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_INT_TO_INT_MAX_DISTANCE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_INT_TO_INT_NON_CONTAINS_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_INT_TO_INT_NON_REMOVE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_INT_TO_INT_WITH_REMOVE_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_INT_TO_LONG_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_INT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_INT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_INT_TO_LONG_NON_CONTAINS_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_INT_TO_LONG_WITH_REMOVE_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_INT_TO_OBJECT_NON_CONTAINS_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LONG_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_LONG_NON_BUCKET_ARRAY_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_LONG_TO_OBJECT_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_LONG_TO_LONG_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_LONG_TO_INT_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_LONG_TO_INT_WITH_REMOVE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_LONG_TO_INT_NON_CONTAINS_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LONG_TO_LONG_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_LONG_TO_LONG_NON_CONTAINS_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LONG_TO_OBJECT_NON_CONTAINS_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_LONG_TO_OBJECT_WITH_REMOVE_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LONG_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_LONG_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_OBJECT_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_OBJECT_TO_OBJECT_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_OBJECT_NON_CONTAINS_KEY_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_OBJECT_WITH_REMOVE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LARGE_LONG_NON_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_INT_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LONG_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_MUTABLE_LONG_TO_INT_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_INT_NON_BUCKET_SET = DEBUG_SETS;

    public static final boolean DEBUG_MUTABLE_INT_MAX_DISTANCE_NON_BUCKET_SET = DEBUG_SETS;

    public static final boolean DEBUG_BASE_INT_BUCKET_SET = DEBUG_SETS;
    public static final boolean DEBUG_MUTABLE_INT_BUCKET_SET = DEBUG_SETS;
    public static final boolean DEBUG_BASE_LONG_BUCKET_SET = DEBUG_SETS;
    public static final boolean DEBUG_MUTABLE_LONG_BUCKET_SET = DEBUG_SETS;

    public static final boolean DEBUG_BASE_LONG_TO_OBJECT_BUCKET_MAP = DEBUG_MAPS;
    public static final boolean DEBUG_BASE_LONG_TO_INT_BUCKET_MAP = DEBUG_MAPS;

    public static final boolean DEBUG_BASE_LARGE_LONG_BUCKET_SET = DEBUG_SETS;
    public static final boolean DEBUG_MUTABLE_LARGE_LONG_BUCKET_SET = DEBUG_SETS;

    public static final boolean DEBUG_BIT_BUFFER_UTIL = Boolean.FALSE;
}
