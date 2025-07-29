package dev.jdata.db.schema.storage.sqloutputter;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public interface ExceptionAppendable<P, E extends Exception> {

    void append(char c, P parameter) throws E;

    default void append(CharSequence charSequence, P parameter) throws E {

        Objects.requireNonNull(charSequence);

        append(charSequence, 0, charSequence.length(), parameter);
    }

    default void append(CharSequence charSequence, int starIndex, int numCharacters, P parameter) throws E {

        Objects.requireNonNull(charSequence);
        Checks.checkFromIndexSize(starIndex, numCharacters, charSequence.length());

        final int endIndex = starIndex + numCharacters;

        for (int i = starIndex; i < endIndex; ++ i) {

            append(charSequence.charAt(i), parameter);
        }
    }
}
