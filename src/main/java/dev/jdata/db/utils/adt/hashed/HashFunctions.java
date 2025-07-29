package dev.jdata.db.utils.adt.hashed;

public class HashFunctions {

    private static int hashKey(int key) {

        return key;
    }

    public static int hashArrayIndex(int key, int keyMask) {

        return hashKey(key) & keyMask;
    }

    private static int hashKey(long key) {

        return ((int)((key >>> 32) | (key & 0x00000000FFFFFFFFL)));
    }

    public static int hashArrayIndex(long key, int keyMask) {

        return hashKey(key) & keyMask;
    }

    public static int objectHashArrayIndex(Object object, int keyMask) {

        return object.hashCode() & keyMask;
    }

    public static long longHashKey(long key) {

        return key;
    }

    public static long longHashArrayIndex(long key, long keyMask) {

        return longHashKey(key) & keyMask;
    }

    public static long longObjectHashArrayIndex(Object object, long keyMask) {

        return object.hashCode() & keyMask;
    }
}
