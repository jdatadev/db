package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.lists.LargeLists;

public class IntBuckets {

    public static final int NO_INT_NODE = LargeLists.NO_INT_NODE;

    public static int checkIntNodeOuterCapacity(int outerCapacity) {

        return LargeLists.checkIntNodeOuterCapacity(outerCapacity);
    }

    public static int checkIntNodeInnerCapacity(int innerCapacity) {

        return LargeLists.checkIntNodeInnerCapacity(innerCapacity);
    }

    public static int nodeToInt(long node) {

        return LargeLists.nodeToInt(node);
    }

    public static long intToNode(int integer) {

        return LargeLists.intToNode(integer);
    }

    public static void clearHashArray(int[] bucketHeadNodesHashArray) {

        Objects.requireNonNull(bucketHeadNodesHashArray);

        Arrays.fill(bucketHeadNodesHashArray, IntBuckets.NO_INT_NODE);
    }
}
