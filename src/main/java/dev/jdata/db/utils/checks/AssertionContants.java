package dev.jdata.db.utils.checks;

public class AssertionContants {

    private static final boolean ASSERT_MAPS = Boolean.FALSE;
    private static final boolean ASSERT_SETS = Boolean.FALSE;
    private static final boolean ASSERT_BIT_BUFFERS = Boolean.FALSE;
    private static final boolean ASSERT_BITS_UTIL = Boolean.TRUE;

    public static final boolean ASSERT_ROW_CACHE = Boolean.FALSE;

    public static final boolean ASSERT_BASE_MAP = ASSERT_MAPS;
    public static final boolean ASSERT_BASE_EXPONENT_MAP = ASSERT_MAPS;
    public static final boolean ASSERT_BASE_LONG_ARRAY_MAP = ASSERT_MAPS;
    public static final boolean ASSERT_LONG_TO_OBJECT_MAP = ASSERT_MAPS;

    public static final boolean ASSERT_INT_SET = ASSERT_SETS;
    public static final boolean ASSERT_LONG_SET = ASSERT_SETS;

//    public static final boolean ASSERT_BIT_BUFFER_SET_VALUE = ASSERT_BIT_BUFFERS;

    public static final boolean ASSERT_BITS_UTIL_GET_RANGE = ASSERT_BITS_UTIL;
}
