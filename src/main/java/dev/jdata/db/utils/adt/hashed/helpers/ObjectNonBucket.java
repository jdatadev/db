package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;

public class ObjectNonBucket {

    static final Object NO_KEY = null;

    public static <T> void clearHashArray(T[] hashArray) {

        Arrays.fill(hashArray, NO_KEY);
    }
}
