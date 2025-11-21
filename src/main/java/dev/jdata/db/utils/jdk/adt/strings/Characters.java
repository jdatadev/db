package dev.jdata.db.utils.jdk.adt.strings;

public class Characters {

    public static boolean areEqualCaseInsensitive(char c1, char c2) {

        if (!isPrintable(c1)) {

            throw new IllegalArgumentException();
        }

        if (!isPrintable(c2)) {

            throw new IllegalArgumentException();
        }

        return Character.toLowerCase(c1) == Character.toLowerCase(c2);
    }

    public static boolean isPrintable(char c) {

        return !Character.isISOControl(c);
    }

    public static boolean areASCIIEqualCaseInsensitive(char c1, char c2) {

        if (!isASCIIAlpha(c1)) {

            throw new IllegalArgumentException();
        }

        if (!isASCIIAlpha(c2)) {

            throw new IllegalArgumentException();
        }

        return Character.toLowerCase(c1) == Character.toLowerCase(c2);
    }

    public static boolean isASCIIAlpha(char c) {

        return
                   (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z');
    }

    public static boolean isASCIIAlphaNumeric(char c) {

        return (c >= '0' && c <= '9') || isASCIIAlpha(c);
    }
}
