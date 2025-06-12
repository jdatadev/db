package dev.jdata.db.engine.server;

import java.nio.CharBuffer;
import java.util.Objects;

import org.jutils.io.loadstream.LoadStream;

final class CharBufferLoadStream extends LoadStream<RuntimeException> {

    private CharBuffer charBuffer;

    void initialize(CharBuffer charBuffer) {

        if (this.charBuffer != null) {

            throw new IllegalStateException();
        }

        this.charBuffer = Objects.requireNonNull(charBuffer);
    }

    void free() {

        if (charBuffer == null) {

            throw new IllegalStateException();
        }

        this.charBuffer = null;
    }

    @Override
    public long read(char[] buffer, int offset, int length) {

        Objects.checkFromIndexSize(offset, length, buffer.length);

        final int toRead = Math.min(charBuffer.remaining(), length);

        charBuffer.get(buffer, offset, toRead);

        return toRead;
    }

    @Override
    public void close() {

    }
}
