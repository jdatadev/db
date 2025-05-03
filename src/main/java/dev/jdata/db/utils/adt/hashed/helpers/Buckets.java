package dev.jdata.db.utils.adt.hashed.helpers;

import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.scalars.Integers;

public class Buckets {

    public static final int NO_INT_NODE = -1;

    @Deprecated
    public static int nodeToInt(long node) {

        return node != BaseList.NO_NODE
                ? (Integers.checkUnsignedLongToUnsignedShort(node >>> 32) << 16) | Integers.checkUnsignedLongToUnsignedShort(node & 0xFFFFFFFFL)
                : (int)BaseList.NO_NODE;
    }

    @Deprecated
    public static long intToNode(int integer) {

        return integer != BaseList.NO_NODE
                ? (((long)integer & 0xFFFF0000) << 16) | (integer & 0x0000FFFF)
                : BaseList.NO_NODE;
    }
}
