package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.checks.Checks;

public class LongNonBucket {

    public static final long NO_ELEMENT = -1L;

    public static void checkIsHashArrayElement(long hashArrayElement) {

        Checks.isNotNegative(hashArrayElement);
    }

    @Deprecated
    public static void clearHashArray(LargeLongArray hashArray) {

        Objects.requireNonNull(hashArray);
        Checks.isTrue(hashArray.hasClearValue());

        hashArray.clear();
    }
}
