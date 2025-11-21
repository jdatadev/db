package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

public class ObjectNonBucket {

    public static final Object NO_ELEMENT = null;

    public static void checkIsHashArrayElement(Object hashArrayElement) {

        Checks.areNotSame(hashArrayElement, NO_ELEMENT);
    }

    public static <T> void clearHashArray(T[] hashArray) {

        Arrays.fill(hashArray, NO_ELEMENT);
    }
}
