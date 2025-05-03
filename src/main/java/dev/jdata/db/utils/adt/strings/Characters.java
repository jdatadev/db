package dev.jdata.db.utils.adt.strings;

public class Characters {

    public static boolean isASCIIAlphaNumeric(char c) {

        return
                   (c >= '0' && c <= '9')
                || (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z');
    }
}
