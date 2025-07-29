package dev.jdata.db.utils.adt;

import dev.jdata.db.utils.checks.Checks;

public class Capacity {

    static int computeArrayOuterCapacity(int capacity, int innerCapacity) {

        Checks.isCapacity(capacity);
        Checks.isCapacityAboveZero(innerCapacity);

        return capacity != 0 ? ((capacity - 1) / innerCapacity) + 1 : 0;
    }

    public static long getRemainderOfLastInnerArrayWithLimit(long limit, long innerElementCapacity, int numOuterUtilizedEntries, int expectedOuterIndex) {

        Checks.areEqual(expectedOuterIndex, numOuterUtilizedEntries != 0 ? numOuterUtilizedEntries - 1 : 0);
        Checks.isArrayLimit(limit);
        Checks.isCapacityAboveZero(innerElementCapacity);
        Checks.areEqual(expectedOuterIndex, limit != 0L ? (limit - 1) / innerElementCapacity : 0);

        final long result;

        if (numOuterUtilizedEntries == 0) {

            result = 0L;
        }
        else if (limit == 0L) {

            result = innerElementCapacity;
        }
        else {
            final long numInnerUtilizedElements = limit % innerElementCapacity;

            result = numInnerUtilizedElements != 0L ? innerElementCapacity - numInnerUtilizedElements : 0L;
        }

        return result;
    }

    @Deprecated // currently in use?
    static long getRemainderOfLastInnerArray(long numInnerUtilizedElements, long innerElementCapacity, int numOuterUtilizedEntries, int expectedOuterIndex) {

        final long result;

        if (numOuterUtilizedEntries == 0L) {

            Checks.isExactlyZero(expectedOuterIndex);

            result = 0L;
        }
        else {
            Checks.areEqual(expectedOuterIndex, numOuterUtilizedEntries - 1);

            result = innerElementCapacity - numInnerUtilizedElements;
        }

        return result;
    }
}
