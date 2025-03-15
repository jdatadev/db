package dev.jdata.db.engine.server;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Objects;

import org.jutils.io.loadstream.LoadStream;

final class CharBufferLoadStream extends LoadStream {

    private CharBuffer charBuffer;

    void initialize(CharBuffer charBuffer) {

        this.charBuffer = Objects.requireNonNull(charBuffer);
    }

    @Override
    public long read(char[] buffer, int offset, int length) throws IOException {

        final int toRead = Math.min(charBuffer.remaining(), length);

        charBuffer.get(buffer, offset, toRead);

        return toRead;
    }

    @Override
    public void close() throws IOException {

    }
}
