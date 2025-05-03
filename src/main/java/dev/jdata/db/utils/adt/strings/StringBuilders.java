package dev.jdata.db.utils.adt.strings;

import java.util.Objects;

public class StringBuilders {

    public static enum Case {

        UPPER,
        LOWER
    }

    static final String HEX_PREFIX = "0x";

    public static void format(StringBuilder sb, String format, Object ... arguments) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(format);

        sb.append(String.format(format, arguments));
    }

    public static void hexString(StringBuilder sb, long value, boolean addPrefix) {

        hexString(sb, value, addPrefix);
    }

    public static void hexString(StringBuilder sb, long value, boolean addPrefix, int zeroPad) {

        hexString(sb, value, addPrefix ? HEX_PREFIX : null, zeroPad, Case.UPPER);
    }

    public static void hexString(StringBuilder sb, long value, CharSequence prefix, int zeroPad, Case hexCase) {

        final int length = sb.length();

        int additionalLength = 0;

        if (prefix != null) {

            additionalLength += prefix.length();
        }

        final long absValue;

        if (value < 0L) {

            ++ additionalLength;

            absValue = - value;
        }
        else {
            absValue = value;
        }

        int digitCounter;

        if (value == 0L) {

            digitCounter = 1;
        }
        else {
            digitCounter = 0;

            for (long v = absValue; v != 0L; v >>>= 4) {

                ++ digitCounter;
            }
        }

        if (zeroPad > 0) {

            additionalLength += Math.max(zeroPad, digitCounter);
        }
        else {
            additionalLength += digitCounter;
        }

        sb.ensureCapacity(length + additionalLength);

        if (prefix != null) {

            sb.append(prefix);
        }

        if (value < 0L) {

            sb.append('-');
        }

        if (zeroPad > 0) {

            for (int i = zeroPad - digitCounter; i > 0; -- i) {

                sb.append('0');
            }
        }

        final char hexBase;

        switch (hexCase) {

        case UPPER:

            hexBase = 'A';
            break;

        case LOWER:

            hexBase = 'a';
            break;

        default:
            throw new UnsupportedOperationException();
        }

        final int numBits = digitCounter << 2;

        for (int i = 0, shift = numBits; i < digitCounter; ++ i) {

            final long mask = (1L << shift) - 1L;

            shift -= 4;

            final int digit = (int)((absValue & mask) >>> shift);

            final char c = (char)(digit < 10 ? '0' + digit : hexBase + (digit - 10));

            sb.append(c);
        }
    }
}
