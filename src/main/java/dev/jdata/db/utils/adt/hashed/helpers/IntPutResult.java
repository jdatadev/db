package dev.jdata.db.utils.adt.hashed.helpers;

public class IntPutResult {

    public static long makePutResult(boolean newAdded, int found) {

        final long result = ((newAdded ? 1L : 0L) << 32) | found;

        return result;
    }

    public static boolean getPutNewAdded(long putResult) {

        return putResult >>> 32 != 0L;
    }

    public static int getPutIndex(long putResult) {

        return (int)(putResult & 0xFFFFFFFFL);
    }
}
