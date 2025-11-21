package dev.jdata.db.utils.jdk.adt.strings;

import java.nio.charset.CharsetEncoder;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public class CharacterEncodingUtil {

    public static int calculateNumEncodedBytes(CharsetEncoder charsetEncoder, int numCharacters) {

        Objects.requireNonNull(charsetEncoder);
        Checks.isNumCharacters(numCharacters);

        return Math.round((numCharacters * charsetEncoder.maxBytesPerChar()) + 0.5f);
    }
}
