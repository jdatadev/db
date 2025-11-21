package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.checks.Checks;

public class LongNonBucket {

    public static final long NO_ELEMENT = Long.MIN_VALUE;

    public static void checkIsHashArrayElement(long hashArrayElement) {

        Checks.areNotEqual(hashArrayElement, NO_ELEMENT);
    }

    public static void clearHashArray(long[] hashArray) {

        Objects.requireNonNull(hashArray);

        Arrays.fill(hashArray, NO_ELEMENT);
    }

    @Deprecated
    public static void clearHashArray(IMutableLongLargeArray hashArray) {

        Objects.requireNonNull(hashArray);
        Checks.isTrue(hashArray.hasClearValue());

        hashArray.clear();
    }
}
