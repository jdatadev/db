package dev.jdata.db.utils.adt.hashed.helpers;

public class LongCapacityPutResult {

    private static final long HIGH_BIT = 1L << 63;

    public static long makePutResult(boolean newAdded, long found) {

        final long highBit = HIGH_BIT;

        if ((found & highBit) != 0L) {

            throw new IllegalArgumentException();
        }

        return (newAdded ? highBit : 0L) | found;
    }

    public static boolean getPutNewAdded(long putResult) {

        return (putResult & HIGH_BIT) != 0L;
    }

    public static long getPutIndex(long putResult) {

        return putResult & (~HIGH_BIT);
    }
}
