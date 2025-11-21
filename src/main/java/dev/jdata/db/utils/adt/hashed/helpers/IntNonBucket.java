package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.IMutableIntLargeArray;
import dev.jdata.db.utils.checks.Checks;

public class IntNonBucket {

    public static final int NO_ELEMENT = Integer.MIN_VALUE;

    public static void checkIsHashArrayElement(int hashArrayElement) {

        Checks.areNotEqual(hashArrayElement, NO_ELEMENT);
    }

    public static void clearHashArray(int[] hashArray) {

        Objects.requireNonNull(hashArray);

        Arrays.fill(hashArray, NO_ELEMENT);
    }

    @Deprecated
    public static void clearHashArray(IMutableIntLargeArray hashArray) {

        Objects.requireNonNull(hashArray);
        Checks.isTrue(hashArray.hasClearValue());

        hashArray.clear();
    }
}
