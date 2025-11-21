package dev.jdata.db.utils.jdk.adt.strings;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CharPredicate;

public class Strings {

    public static String nullOrNonNullString(Object object) {

        return object != null ? "<non-null>" : "<null>";
    }

    public static String repeat(String string, int times) {

        Checks.isNotEmpty(string);
        Checks.isAboveZero(times);

        final int stringLength = string.length();
        final int resultLength = stringLength * times;

        final char[] charArray = new char[resultLength];

        int dstIndex = 0;

        for (int i = 0; i < times; ++ i) {

            for (int j = 0; j < stringLength; ++ j) {

                charArray[dstIndex ++] = string.charAt(j);
            }
        }

        return new String(charArray);
    }

    public static String repeat(String string, int times, char separator) {

        Checks.isNotEmpty(string);
        Checks.isAboveZero(times);

        final int stringLength = string.length();
        final int resultLength = ((stringLength + 1) * times) - 1;

        final char[] charArray = new char[resultLength];

        int dstIndex = 0;

        for (int i = 0; i < times; ++ i) {

            for (int j = 0; j < stringLength; ++ j) {

                if (j == 0 && dstIndex > 0) {

                    charArray[dstIndex ++] = separator;
                }

                charArray[dstIndex ++] = string.charAt(j);
            }
        }

        return new String(charArray);
    }

    public static boolean isAllLowerCase(String string) {

        return containsOnly(string, Character::isLowerCase);
    }

    public static boolean containsOnly(String string, CharPredicate predicate) {

        return stringContainsOnly(string, 0, string.length(), predicate);
    }

    public static String containsOnly(String string, int startIndex, int numCharacters, CharPredicate predicate) {

        if (!stringContainsOnly(string, startIndex, numCharacters, predicate)) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static boolean containsAny(String string, CharPredicate predicate) {

        return CharSequences.containsAny(string, predicate);
    }

    public static boolean containsAny(String string, int startIndex, int numCharacters, CharPredicate predicate) {

        return CharSequences.containsAny(string, startIndex, numCharacters, predicate);
    }

    public static boolean isASCIIAlphaNumeric(String string) {

        return CharSequences.isASCIIAlphaNumeric(string);
    }

    public static boolean isASCIIAlphaNumeric(String string, CharPredicate additionalPredicate) {

        return CharSequences.isASCIIAlphaNumeric(string, additionalPredicate);
    }

    private static boolean stringContainsOnly(String string, int startIndex, int numCharacters, CharPredicate predicate) {

        return CharSequences.containsOnly(string, startIndex, numCharacters, predicate);
    }

    public static boolean stringContainsAny(String string, CharPredicate predicate) {

        return stringContainsAny(string, 0, string.length(), predicate);
    }

    private static boolean stringContainsAny(String string, int startIndex, int numCharacters, CharPredicate predicate) {

        return CharSequences.containsAny(string, startIndex, numCharacters, predicate);
    }

    public static boolean hasFirstCharacterAndRemaining(CharSequence charSequence, int numCharacters, CharPredicate firstCharacterPredicate,
            CharPredicate remainingCharacterspredicate) {

        return CharSequences.hasFirstCharacterAndRemaining(charSequence, numCharacters, firstCharacterPredicate, remainingCharacterspredicate);
    }

    public static String repeat(char c, int length) {

        Checks.isNotNegative(length);

        final char[] chars = new char[length];

        Arrays.fill(chars, c);

        return String.copyValueOf(chars);
    }

    public static <T extends CharSequence> void join(Iterable<T> toJoin, char separator, StringBuilder sb) {

        join(toJoin, separator, sb, (e, b) -> b.append(e));
    }

    public static <T> void join(Iterable<T> toJoin, char separator, StringBuilder sb, BiConsumer<T, StringBuilder> adder) {

        Objects.requireNonNull(toJoin);
        Objects.requireNonNull(sb);
        Objects.requireNonNull(adder);

        boolean first = true;

        for (T value : toJoin) {

            if (first) {

                first = false;
            }
            else {
                sb.append(separator);
            }

            adder.accept(value, sb);
        }
    }

    public static <T extends CharSequence> void join(T[] toJoin, char separator, StringBuilder sb) {

        join(toJoin, separator, sb, (e, b) -> b.append(e));
    }

    public static <T> void join(T[] toJoin, char separator, StringBuilder sb, BiConsumer<T, StringBuilder> adder) {

        Checks.isNotEmpty(toJoin);
        Objects.requireNonNull(sb);
        Objects.requireNonNull(adder);

        boolean first = true;

        for (T value : toJoin) {

            if (first) {

                first = false;
            }
            else {
                sb.append(separator);
            }

            adder.accept(value, sb);
        }
    }

    public static String of(CharSequence charSequence) {

        Objects.requireNonNull(charSequence);

        final int length = charSequence.length();

        final char[] charArray = new char[length];

        for (int i = 0; i < length; ++ i) {

            charArray[i] = charSequence.charAt(i);
        }

        return String.copyValueOf(charArray);
    }

    public static String of(CharSequence charSequence, int startIndex, int numCharacters) {

        Objects.requireNonNull(charSequence);
        Checks.checkFromIndexSize(startIndex, numCharacters, charSequence.length());

        final char[] charArray = new char[numCharacters];

        for (int i = 0; i < numCharacters; ++ i) {

            charArray[i] = charSequence.charAt(startIndex + i);
        }

        return String.copyValueOf(charArray);
    }

    public static String binaryString(byte value, boolean addPrefix) {

        return BitsUtil.binaryString(value, addPrefix);
    }

    public static String binaryString(short value, boolean addPrefix) {

        return binaryString(Short.toUnsignedInt(value), addPrefix);
    }

    public static String binaryString(int value, boolean addPrefix) {

        final String binaryString = Integer.toBinaryString(value);

        return addPrefix ? BitsUtil.BINARY_PREFIX + binaryString : binaryString;
    }

    public static String binaryString(long value, boolean addPrefix) {

        final String binaryString = Long.toBinaryString(value);

        return addPrefix ? BitsUtil.BINARY_PREFIX + binaryString : binaryString;
    }

    public static String hexString(long value) {

        return hexString(value, false);
    }

    public static String hexString(long value, boolean addPrefix) {

        final String hexString = Long.toHexString(value);

        return addPrefix ? StringBuilders.HEX_PREFIX + hexString : hexString;
    }

    public static String hexString(long value, int zeroPad) {

        int capacity = 30;

        if (zeroPad > 0) {

            capacity += zeroPad;
        }

        final StringBuilder sb = new StringBuilder(capacity);

        StringBuilders.hexString(sb, value, true, zeroPad);

        return sb.toString();
    }
}
