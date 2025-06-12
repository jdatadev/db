package dev.jdata.db.utils.scalars;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public class Integers {

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

    public static short checkUnsignedIntToUnsignedShort(int value) {

        Checks.isNotNegative(value);

        if (value > Short.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (short)value;
    }

    public static int checkLongToInt(long value) {

        if (value < Integer.MIN_VALUE) {

            throw new IllegalArgumentException();
        }
        else if (value > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (int)value;
    }

    public static short checkUnsignedLongToUnsignedShort(long value) {

        Checks.isNotNegative(value);

        if (value > Short.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (short)value;
    }

    public static int checkUnsignedLongToUnsignedInt(long value) {

        Checks.isNotNegative(value);

        if (value > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (int)value;
    }

    @FunctionalInterface
    public interface CharAdder<P, E extends Exception> {

        void add(char c, P parameter) throws E;
    }

    public static <P, E extends Exception> void toChars(long integer, P parameter, CharAdder<P, E> charAdder) throws E {

        Objects.requireNonNull(charAdder);

        if (integer == 0L) {

            charAdder.add('0', parameter);
        }
        else if (integer == Long.MIN_VALUE) {

            charAdder.add('-', parameter);

            toCharNegative(integer, parameter, charAdder);
        }
        else {
            final long unsigned;

            if (integer < 0L) {

                charAdder.add('-', parameter);

                unsigned = -integer;
            }
            else {
                unsigned = integer;
            }

            toCharsUnsigned(unsigned, parameter, charAdder);
        }
    }

    private static <P, E extends Exception> void toCharNegative(long integer, P parameter, CharAdder<P, E> charAdder) throws E {

        if (integer != 0L) {

            toCharNegative(integer / 10, parameter, charAdder);

            charAdder.add((char)('0' - (integer % 10)), parameter);
        }
    }

    private static <P, E extends Exception> void toCharsUnsigned(long integer, P parameter, CharAdder<P, E> charAdder) throws E {

        if (integer != 0L) {

            toCharsUnsigned(integer / 10, parameter, charAdder);

            charAdder.add((char)('0' + (integer % 10)), parameter);
        }
    }
}
