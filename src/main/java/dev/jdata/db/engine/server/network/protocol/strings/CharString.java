package dev.jdata.db.engine.server.network.protocol.strings;

public class CharString implements CharSequence {

    char[] charArray;
    int numCharacters;

    @Override
    public final int length() {

        return numCharacters;
    }

    @Override
    public final char charAt(int index) {

        if (index >= numCharacters) {

            throw new IllegalArgumentException();
        }

        return charArray[index];
    }

    @Override
    public final CharSequence subSequence(int start, int end) {

        throw new UnsupportedOperationException();
    }

    @Deprecated // pass string cache?
    public final String asString() {

        return String.copyValueOf(charArray, 0, numCharacters);
    }
}
