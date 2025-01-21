package dev.jdata.db;

public enum LockType {

    READ,
    WRITE;

    public static LockType ofOrdinal(int ordinal) {

        return LockType.values()[ordinal];
    }
}
