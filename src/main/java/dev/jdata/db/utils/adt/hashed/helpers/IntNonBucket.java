package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.LargeIntArray;
import dev.jdata.db.utils.checks.Checks;

public class IntNonBucket {

    public static final int NO_ELEMENT = -1;

    public static void checkIsHashArrayElement(int hashArrayElement) {

        Checks.isNotNegative(hashArrayElement);
    }

    public static void clearHashArray(int[] hashArray) {

        Objects.requireNonNull(hashArray);

        Arrays.fill(hashArray, NO_ELEMENT);
    }

    @Deprecated
    public static void clearHashArray(LargeIntArray hashArray) {

        Objects.requireNonNull(hashArray);
        Checks.isTrue(hashArray.hasClearValue());

        hashArray.clear();
    }
}
