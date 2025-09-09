package dev.jdata.db.utils.checks;

public class AssertionContants {

    private static final boolean ASSERT_SCHEMA = Boolean.TRUE;

    public static final boolean ASSERT_DB_NAMED_OBJECT_MAP = ASSERT_SCHEMA;
    public static final boolean ASSERT_SCHEMA_MAP = ASSERT_SCHEMA;

    private static final boolean ASSERT_SESSION = Boolean.TRUE;

    public static final boolean ASSERT_DB_SESSION = ASSERT_SESSION;

    private static final boolean ASSERT_TRANSACTION_MANAGEMENT = Boolean.TRUE;

    public static final boolean ASSERT_TRANSACTION = ASSERT_TRANSACTION_MANAGEMENT;
    public static final boolean ASSERT_TRANSACTION_LOCKING = ASSERT_TRANSACTION_MANAGEMENT;

    public static final boolean ASSERT_MVCC_ROW_BUFFER_COMPARER = Boolean.TRUE;

    public static final boolean ASSERT_MVCC = Boolean.TRUE;

    private static final boolean ASSERT_LOCKING = Boolean.TRUE;

    public static final boolean ASSERT_LOCK_TABLE = ASSERT_LOCKING;

    private static final boolean ASSERT_ARRAY = Boolean.TRUE;
    private static final boolean ASSERT_HASHED = Boolean.TRUE;
    private static final boolean ASSERT_MAPS = Boolean.TRUE;
    private static final boolean ASSERT_SETS = Boolean.TRUE;

    public static final boolean ASSERT_BIT_BUFFER = Boolean.TRUE;
    private static final boolean ASSERT_BITS_UTIL = Boolean.TRUE;

    public static final boolean ASSERT_ROW_CACHE = Boolean.TRUE;

    public static final boolean ASSERT_BASE_ARRAY = ASSERT_ARRAY;
    public static final boolean ASSERT_BASE_ANY_LARGE_ARRAY = ASSERT_ARRAY;

    public static final boolean ASSERT_HASH_ARRAY = ASSERT_HASHED;
    public static final boolean ASSERT_LARGE_HASH_ARRAY = ASSERT_HASHED;

    public static final boolean ASSERT_BASE_CAPACITY_HASHED = ASSERT_HASHED;

    public static final boolean ASSERT_BASE_MAP = ASSERT_MAPS;
    public static final boolean ASSERT_BASE_EXPONENT_MAP = ASSERT_MAPS;

    public static final boolean ASSERT_INT_TO_OBJECT_NON_BUCKET_MAP = ASSERT_MAPS;

    public static final boolean ASSERT_LONG_TO_OBJECT_NON_BUCKET_MAP = ASSERT_MAPS;

    public static final boolean ASSERT_BASE_INT_CAPACITY_EXPONENT_ARRAY_HASHED = ASSERT_MAPS;
    public static final boolean ASSERT_BASE_LONG_CAPACITY_EXPONENT_ARRAY_HASHED = ASSERT_MAPS;

    public static final boolean ASSERT_BITS_UTIL_GET_RANGE = ASSERT_BITS_UTIL;
}
