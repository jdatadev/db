package dev.jdata.db.utils.scalars;

import dev.jdata.db.utils.checks.Checks;

public class Integers {

    public static int checkUnsignedLongToUnsignedInt(long value) {

        Checks.isNotNegative(value);

        if (value > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (int)value;
    }
}
