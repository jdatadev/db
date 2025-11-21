package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.lists.LargeNodeLists;
import dev.jdata.db.utils.checks.Checks;

public class LongBuckets {

    public static void checkIsHashArrayElement(long hashArrayElement) {

        Checks.isNotNegative(hashArrayElement);
    }

    public static void clearHashArray(long[] bucketHeadNodesHashArray) {

        Objects.requireNonNull(bucketHeadNodesHashArray);

        Arrays.fill(bucketHeadNodesHashArray, LargeNodeLists.NO_LONG_NODE);
    }

    public static void clearHashArray(IMutableLongLargeArray bucketHeadNodesHashArray) {

        Objects.requireNonNull(bucketHeadNodesHashArray);
        Checks.isTrue(bucketHeadNodesHashArray.hasClearValue());

        bucketHeadNodesHashArray.clear();
    }
}
