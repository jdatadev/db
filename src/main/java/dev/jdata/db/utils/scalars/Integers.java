package dev.jdata.db.utils.scalars;

import dev.jdata.db.utils.checks.Checks;

public class Integers {

    public static int checkLongToInt(long value) {

        if (value < Integer.MIN_VALUE) {

            throw new IllegalArgumentException();
        }
        else if (value > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (int)value;
    }

    public static int checkUnsignedLongToUnsignedInt(long value) {

        Checks.isNotNegative(value);

        if (value > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (int)value;
    }

    public static byte checkIntToByte(int value) {

        if (value < Byte.MIN_VALUE) {

            throw new IllegalArgumentException();
        }
        else if (value > Byte.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (byte)value;
    }

    public static byte checkUnsignedIntToUnsignedByte(int value) {

        Checks.isNotNegative(value);

        if (value > 255) {

            throw new IllegalArgumentException();
        }

        return (byte)value;
    }
}
