package dev.jdata.db.utils.checks;

public class AssertionContants {

    private static final boolean ASSERT_SESSION = Boolean.TRUE;

    public static final boolean ASSERT_DB_SESSION = ASSERT_SESSION;

    private static final boolean ASSERT_TRANSACTION_MANAGEMENT = Boolean.TRUE;

    public static final boolean ASSERT_TRANSACTION = ASSERT_TRANSACTION_MANAGEMENT;
    public static final boolean ASSERT_TRANSACTION_LOCKING = ASSERT_TRANSACTION_MANAGEMENT;

    public static final boolean ASSERT_MVCC_ROW_BUFFER_COMPARER = Boolean.TRUE;

    public static final boolean ASSERT_MVCC = Boolean.TRUE;

    private static final boolean ASSERT_LOCKING = Boolean.TRUE;

    public static final boolean ASSERT_LOCK_TABLE = ASSERT_LOCKING;

    private static final boolean ASSERT_MAPS = Boolean.FALSE;
    private static final boolean ASSERT_SETS = Boolean.FALSE;
    private static final boolean ASSERT_BIT_BUFFERS = Boolean.FALSE;
    private static final boolean ASSERT_BITS_UTIL = Boolean.FALSE;

    public static final boolean ASSERT_ROW_CACHE = Boolean.FALSE;

    public static final boolean ASSERT_BASE_MAP = ASSERT_MAPS;
    public static final boolean ASSERT_BASE_EXPONENT_MAP = ASSERT_MAPS;

    public static final boolean ASSERT_BASE_INT_MAP = ASSERT_MAPS;
    public static final boolean ASSERT_INT_TO_OBJECT_MAP = ASSERT_MAPS;

    public static final boolean ASSERT_BASE_LONG_MAP = ASSERT_MAPS;
    public static final boolean ASSERT_LONG_TO_OBJECT_MAP = ASSERT_MAPS;

    public static final boolean ASSERT_INT_SET = ASSERT_SETS;
    public static final boolean ASSERT_LONG_SET = ASSERT_SETS;

    public static final boolean ASSERT_BASE_OBJECT_MAP = ASSERT_MAPS;

//    public static final boolean ASSERT_BIT_BUFFER_SET_VALUE = ASSERT_BIT_BUFFERS;

    public static final boolean ASSERT_BITS_UTIL_GET_RANGE = ASSERT_BITS_UTIL;
}
