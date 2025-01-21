package dev.jdata.db;

import dev.jdata.db.utils.bits.BitsUtil;

public class DBBitEncoding {

    protected static final int LOCK_TYPE_NUM_BITS;

    static {

        LOCK_TYPE_NUM_BITS = BitsUtil.getNumEnumBits(LockType.class);

        if (LOCK_TYPE_NUM_BITS > 1) {

            throw new IllegalStateException();
        }
    }

    private static final int NUM_ENCODED_ROW_BITS = Long.SIZE;
    private static final int NUM_ENCODED_TABLE_ID_ROW_ID_BITS = NUM_ENCODED_ROW_BITS - LOCK_TYPE_NUM_BITS;

    private static final int TABLE_ID_NUM_BITS = 16;
    private static final int ROW_ID_NUM_BITS = NUM_ENCODED_TABLE_ID_ROW_ID_BITS - TABLE_ID_NUM_BITS;

    private static final int TABLE_ID_SHIFT = ROW_ID_NUM_BITS;
    private static final int LOCK_TYPE_SHIFT = TABLE_ID_NUM_BITS + LOCK_TYPE_NUM_BITS;

    private static final long LOCK_TYPE_MASK = BitsUtil.maskLong(LOCK_TYPE_NUM_BITS, LOCK_TYPE_SHIFT);
    private static final long TABLE_ID_MASK = BitsUtil.maskLong(TABLE_ID_NUM_BITS, TABLE_ID_SHIFT);
    private static final long ROW_ID_MASK = BitsUtil.maskLong(ROW_ID_NUM_BITS, 0);

    protected static final int MAX_TABLE_ID = 1 << TABLE_ID_NUM_BITS;
    protected static final long MAX_ROW_ID = 1L << ROW_ID_NUM_BITS;

    public static int getHashTableId(long key) {

        return (int)(key >>> TABLE_ID_SHIFT);
    }

    public static long getHashRowId(long key) {

        return key & ROW_ID_MASK;
    }

    protected static long makeHashKey(int tableId, long rowId) {

        return ((long)tableId) << TABLE_ID_SHIFT | rowId;
    }

    public static int decodeLockTableId(long key) {

        return (int)((key & TABLE_ID_MASK) >>> TABLE_ID_SHIFT);
    }

    public static long decodeLockRowId(long encodedRowLock) {

        return encodedRowLock & ROW_ID_MASK;
    }

    public static LockType decodeLockType(long encodedRowLock) {

        return LockType.ofOrdinal((int)(encodedRowLock >>> LOCK_TYPE_SHIFT));
    }

    public static long encodeRowLock(int tableId, long rowId, LockType lockType) {

        return (((long)tableId) << TABLE_ID_SHIFT) | rowId | (lockType.ordinal() << LOCK_TYPE_SHIFT);
    }
}
