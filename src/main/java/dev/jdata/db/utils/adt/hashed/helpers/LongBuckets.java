package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.lists.LargeLists;
import dev.jdata.db.utils.checks.Checks;

public class LongBuckets {

    public static void checkIsHashArrayElement(long hashArrayElement) {

        Checks.isNotNegative(hashArrayElement);
    }

    public static void clearHashArray(long[] bucketHeadNodesHashArray) {

        Objects.requireNonNull(bucketHeadNodesHashArray);

        Arrays.fill(bucketHeadNodesHashArray, LargeLists.NO_LONG_NODE);
    }

    public static void clearHashArray(LargeLongArray bucketHeadNodesHashArray) {

        Objects.requireNonNull(bucketHeadNodesHashArray);
        Checks.isTrue(bucketHeadNodesHashArray.hasClearValue());

        bucketHeadNodesHashArray.clear();
    }
}
