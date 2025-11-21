package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.lists.IntCapacityNodeLists;

public class IntBuckets {

    public static final int NO_INT_NODE = IntCapacityNodeLists.NO_INT_NODE;

    public static int checkIntNodeOuterCapacity(int outerCapacity) {

        return IntCapacityNodeLists.checkIntNodeOuterCapacity(outerCapacity);
    }

    public static int checkIntNodeInnerCapacity(int innerCapacity) {

        return IntCapacityNodeLists.checkIntNodeInnerCapacity(innerCapacity);
    }

    public static int nodeToInt(long node) {

        return IntCapacityNodeLists.nodeToInt(node);
    }

    public static long intToNode(int integer) {

        return IntCapacityNodeLists.intToNode(integer);
    }

    public static void clearHashArray(int[] bucketHeadNodesHashArray) {

        Objects.requireNonNull(bucketHeadNodesHashArray);

        Arrays.fill(bucketHeadNodesHashArray, IntBuckets.NO_INT_NODE);
    }
}
