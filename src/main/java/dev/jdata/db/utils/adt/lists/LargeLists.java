package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.scalars.Integers;

public class LargeLists {

    public static final int NO_INT_NODE = -1;

    public static final long NO_LONG_NODE = -1L;

    public static int checkIntNodeOuterCapacity(int outerCapacity) {

        return checkIntNodeCapacity(outerCapacity);
    }

    public static int checkIntNodeInnerCapacity(int innerCapacity) {

        return checkIntNodeCapacity(innerCapacity);
    }

    private static int checkIntNodeCapacity(int capacity) {

        if (capacity < 1 || capacity >= 1 << 16) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static int nodeToInt(long node) {

        return node != NO_LONG_NODE
                ? (Integers.checkUnsignedLongToUnsignedShort(node >>> 32) << 16) | Integers.checkUnsignedLongToUnsignedShort(node & 0xFFFFFFFFL)
                : (int)NO_LONG_NODE;
    }

    public static long intToNode(int integer) {

        return integer != NO_LONG_NODE
                ? (((long)integer & 0xFFFF0000) << 16) | (integer & 0x0000FFFF)
                : NO_LONG_NODE;
    }
}
