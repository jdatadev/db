package dev.jdata.db.utils.scalars;

import java.util.Objects;

import org.jutils.io.strings.StringParse;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.jdk.adt.strings.StringBuilders;

public class Integers {

    public static void toHexUnsigned(int i, StringBuilder sb) {

        Objects.requireNonNull(sb);

        final long unsigned = Integer.toUnsignedLong(i);

        StringBuilders.hexString(sb, unsigned, false, 16);
    }

    public static void toHexUnsigned(long l, StringBuilder sb) {

        Objects.requireNonNull(sb);

        StringBuilders.hexString(sb, l, false, 32);
    }

    public static int parseUnsignedInt(String string) {

        return parseUnsignedInt(string, 0, string.length(), 10);
    }

    public static int parseUnsignedInt(CharSequence charSequence) {

        return parseUnsignedInt(charSequence, 0, charSequence.length(), 10);
    }

    public static int parseUnsignedInt(CharSequence charSequence, int startIndex, int endIndex, int radix) {

        int numLeadingZeros = 0;

        for (int i = startIndex; i < endIndex; ++ i) {

            if (charSequence.charAt(i) == '0') {

                ++ numLeadingZeros;
            }
            else {
                break;
            }
        }

        if (numLeadingZeros > 0) {

            if (numLeadingZeros > 1) {

                throw new NumberFormatException();
            }
            else {
                if (charSequence.length() != 1) {

                    throw new NumberFormatException();
                }
            }
        }

        return StringParse.parseUnsignedInt(charSequence, startIndex, endIndex, radix);
    }

    public static int parseSignedInt(String string) {

        return parseSignedInt(string, 0, string.length(), 10);
    }

    public static int parseSignedInt(CharSequence charSequence) {

        return parseSignedInt(charSequence, 0, charSequence.length(), 10);
    }

    public static int parseSignedInt(CharSequence charSequence, int startIndex, int endIndex, int radix) {

        final int charSequenceLength = charSequence.length();

        Checks.checkFromToIndex(startIndex, endIndex, charSequenceLength);

        if (charSequenceLength == 0) {

            throw new NumberFormatException();
        }

        final int result;

        if (charSequence.charAt(startIndex) == '-') {

            if (charSequenceLength == 1) {

                throw new NumberFormatException();
            }

            result = - parseUnsignedInt(charSequence, startIndex + 1, endIndex, radix);
        }
        else {
            result = parseUnsignedInt(charSequence, startIndex, endIndex, radix);
        }

        return result;
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

    public static byte checkUnsignedIntToUnsignedByteAsByte(int value) {

        Checks.isNotNegative(value);

        if (value > 255) {

            throw new IllegalArgumentException();
        }

        return (byte)value;
    }

    public static short checkUnsignedIntToUnsignedByteAsShort(int value) {

        Checks.isNotNegative(value);

        if (value > 255) {

            throw new IllegalArgumentException();
        }

        return (short)value;
    }

    public static short checkIntToShort(int value) {

        if (value < Short.MIN_VALUE) {

            throw new IllegalArgumentException();
        }
        else if (value > Short.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (short)value;
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

    public static byte checkUnsignedLongToUnsignedByteAsByte(long value) {

        Checks.isNotNegative(value);

        if (value > 255) {

            throw new IllegalArgumentException();
        }

        return (byte)value;
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
