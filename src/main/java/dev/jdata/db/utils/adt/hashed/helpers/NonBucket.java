package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

public class NonBucket {

    public static final int NO_ELEMENT = -1;

    public static void checkIsHashArrayElement(int hashArrayElement) {

        Checks.isNotNegative(hashArrayElement);
    }

    public static void checkIsHashArrayElement(long hashArrayElement) {

        Checks.isNotNegative(hashArrayElement);
    }

    public static void clearHashArray(int[] bucketHeadNodesHashArray) {

        Arrays.fill(bucketHeadNodesHashArray, NO_ELEMENT);
    }
}
