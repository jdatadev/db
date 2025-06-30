package dev.jdata.db.utils.adt;

import dev.jdata.db.utils.checks.Checks;

public class Capacity {

    public static int computeArrayOuterCapacity(int initialCapacity, int innerCapacity) {

        return ((initialCapacity - 1) / innerCapacity) + 1;
    }

    public static long getRemainderOfLastInnerArrayWithLimit(int expectedOuterIndex, long limit, int numOuterUtilizedEntries, long innerElementCapacity) {

        Checks.areEqual(expectedOuterIndex, numOuterUtilizedEntries - 1);
        Checks.isLengthAboveOrAtZero(limit);
        Checks.isCapacity(innerElementCapacity);

        Checks.areEqual(expectedOuterIndex, (limit - 1) / innerElementCapacity);

        final long numInnerUtilizedElements = limit % innerElementCapacity;

        return getRemainderOfLastInnerArray(expectedOuterIndex, numOuterUtilizedEntries, innerElementCapacity, numInnerUtilizedElements);
    }

    public static long getRemainderOfLastInnerArray(int expectedOuterIndex, int numOuterUtilizedEntries, long innerElementCapacity, long numInnerUtilizedElements) {

        Checks.areEqual(expectedOuterIndex, numOuterUtilizedEntries - 1);

        return numInnerUtilizedElements != 0L ? innerElementCapacity - numInnerUtilizedElements : 0L;
    }
}
