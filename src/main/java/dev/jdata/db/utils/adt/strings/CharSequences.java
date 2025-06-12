package dev.jdata.db.utils.adt.strings;

import java.util.Objects;

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

    public static boolean isASCIIAlphaNumeric(CharSequence string, CharPredicate additionalPredicate) {

        return containsOnly(string, c ->
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

    private static boolean containsAny(CharSequence charSequence, CharPredicate predicate) {

        return containsAny(charSequence, 0, charSequence.length(), predicate);
    }

    public static boolean containsAny(CharSequence charSequence, int startIndex, int numCharacters, CharPredicate predicate) {

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
}
