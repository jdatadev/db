package dev.jdata.db.engine.server;

import java.util.ConcurrentModificationException;
import java.util.Objects;

import org.jutils.io.loadstream.LoadStream;
import org.jutils.io.loadstream.StreamStatus;

public final class CharSequenceLoadStream extends LoadStream<RuntimeException> {

    private CharSequence charSequence;
    private int charSequenceLength;
    private int charSequenceOffset;

    public CharSequenceLoadStream() {

        this.charSequence = null;
    }

    public CharSequenceLoadStream(CharSequence charSequence) {

        initialize(charSequence);
    }

    public void initialize(CharSequence charSequence) {

        if (this.charSequence != null) {

            throw new IllegalStateException();
        }

        this.charSequence = Objects.requireNonNull(charSequence);
        this.charSequenceLength = charSequence.length();
        this.charSequenceOffset = 0;
    }

    public void reset() {

        if (charSequence == null) {

            throw new IllegalStateException();
        }

        this.charSequence = null;
        this.charSequenceOffset = -1;
    }

    @Override
    public long read(char[] buffer, int offset, int length) {

        Objects.requireNonNull(buffer);
        Objects.checkFromIndexSize(offset, length, buffer.length);

        if (charSequence.length() != charSequenceLength) {

            throw new ConcurrentModificationException();
        }

        final long result;

        if (charSequenceOffset > charSequenceLength) {

            throw new IllegalStateException();
        }
        else if (charSequenceOffset == charSequenceLength) {

            result = StreamStatus.eof();
        }
        else {
            final int remaining = charSequenceLength - charSequenceOffset;

            final int toRead = Math.min(remaining, length);

            final int srcOffset = charSequenceOffset;

            for (int i = 0; i < toRead; ++ i) {

                buffer[offset + i] = charSequence.charAt(srcOffset + i);
            }

            charSequenceOffset += toRead;

            result = toRead;
        }

        return result;
    }

    @Override
    public void close() {

    }
}
