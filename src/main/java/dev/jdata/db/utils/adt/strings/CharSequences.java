package dev.jdata.db.utils.adt.strings;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CharPredicate;

public class CharSequences {

    public static boolean startsWith(CharSequence charSequence, CharSequence other) {

        Objects.requireNonNull(charSequence);
        Objects.requireNonNull(other);

        final int charSequenceLength = charSequence.length();
        final int otherLength = other.length();

        final boolean result;

        if (charSequenceLength == 0) {

            result = false;
        }
        else if (otherLength == 0) {

            result = false;
        }
        else if (otherLength > charSequenceLength) {

            result = false;
        }
        else {
            boolean startsWith = true;

            for (int i = 0; i < otherLength; ++ i) {

                if (charSequence.charAt(i) != other.charAt(i)) {

                    startsWith = false;
                    break;
                }
            }

            result = startsWith;
        }

        return result;
    }

    public static boolean isASCIIAlphaNumeric(CharSequence charSequence) {

        return isASCIIAlphaNumeric(charSequence, c -> false);
    }

    public static boolean isASCIIAlphaNumeric(CharSequence charSequence, CharPredicate additionalPredicate) {

        Checks.isNotEmpty(charSequence);
        Objects.requireNonNull(additionalPredicate);

        return containsOnly(charSequence, c ->
                   Characters.isASCIIAlphaNumeric(c)
                || additionalPredicate.test(c));
    }

    private static boolean containsOnly(CharSequence charSequence, CharPredicate predicate) {

        return containsOnly(charSequence, 0, charSequence.length(), predicate);
    }

    public static boolean containsOnly(CharSequence charSequence, int startIndex, int numCharacters, CharPredicate predicate) {

        boolean containsOnly = true;

        for (int i = 0; i < numCharacters; ++ i) {

            if (!predicate.test(charSequence.charAt(startIndex + i))) {

                containsOnly = false;
                break;
            }
        }

        return containsOnly;
    }

    public static boolean containsAny(CharSequence charSequence, CharPredicate predicate) {

        return containsAny(charSequence, 0, charSequence.length(), predicate);
    }

    public static boolean containsAny(CharSequence charSequence, int startIndex, int numCharacters, CharPredicate predicate) {

        Objects.requireNonNull(charSequence);
        Checks.checkFromIndexNum(startIndex, numCharacters, charSequence.length());

        boolean containsAny = false;

        for (int i = 0; i < numCharacters; ++ i) {

            if (predicate.test(charSequence.charAt(startIndex + i))) {

                containsAny = true;
                break;
            }
        }

        return containsAny;
    }

    public static boolean hasFirstCharacterAndRemaining(CharSequence charSequence, CharPredicate firstCharacterPredicate, CharPredicate remainingCharacterspredicate) {

        return hasFirstCharacterAndRemaining(charSequence, charSequence.length(), firstCharacterPredicate, remainingCharacterspredicate);
    }

    public static boolean hasFirstCharacterAndRemaining(CharSequence charSequence, int numCharacters, CharPredicate firstCharacterPredicate,
            CharPredicate remainingCharacterspredicate) {

        final boolean result;

        if (numCharacters == 0) {

            result = false;
        }
        else {
            if (!firstCharacterPredicate.test(charSequence.charAt(0))) {

                result = false;
            }
            else {
                result = numCharacters > 1
                        ? containsOnly(charSequence, 1, numCharacters - 1, remainingCharacterspredicate)
                        : true;
            }
        }

        return result;
    }

    public static long longHashCode(CharSequence charSequence, int hashArrayCapacityExponent) {

        return longHashCode(charSequence, 0, charSequence.length(), hashArrayCapacityExponent);
    }

    public static long longHashCode(CharSequence charSequence, int startIndex, int numCharacters, int hashArrayCapacityExponent) {

        long hash;

        int hashLength = numCharacters;

        if (numCharacters == 1) {

            hash = charSequence.charAt(startIndex);
        }
        else {
            hash = 0L;

            final int shift;

            final int numHashArrayBits = hashArrayCapacityExponent + 1;

            if (numHashArrayBits <= 7) {

                shift = 0;
            }
            else {
                final int lengthBits = numCharacters << 3;

                if (lengthBits <= numHashArrayBits) {

                    shift = 8;
                }
                else {
                    if (lengthBits >= numHashArrayBits >>> 2) {

                        final int numHashArrayBitsTimesSixteen = numHashArrayBits << 4;

                        if (lengthBits >= numHashArrayBitsTimesSixteen) {

                            shift = 1;

                            hashLength = numHashArrayBitsTimesSixteen >>> 3;
                        }
                        else if (lengthBits >= numHashArrayBits << 3) {

                            shift = 2;
                        }
                        else {
                            shift = 3;
                        }
                    }
                    else {
                        if (lengthBits >= numHashArrayBits >>> 1) {

                            shift = 4;
                        }
                        else {
                            shift = 6;
                        }
                    }
                }
            }

            for (int i = 0; i < hashLength; ++ i) {

                final char c = charSequence.charAt(startIndex + i);

                if (c > 127) {

                    throw new IllegalArgumentException();
                }

                hash <<= shift;
                hash ^= c;
            }
        }

        return hash;
    }
}
