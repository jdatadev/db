package dev.jdata.db.sql.parse;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;

import dev.jdata.db.utils.function.CheckedExceptionBiConsumer;

public interface ISQLString {

    public static final long EOF = -1L;

    void append(Appendable appendable) throws IOException;

    long getLength();

    void write(DataOutput dataOutput) throws IOException;

    long writeToCharBuffer(CharBuffer dst, long offset);

    default String asString() {

        final StringBuilder sb = new StringBuilder(10000);

        try {
            append(sb);
        }
        catch (IOException ex) {

            throw new UnsupportedOperationException(ex);
        }

        return sb.toString();
    }

    default <T, E extends Exception> void writeSQLString(T output, CharsetEncoder charsetEncoder, CharBuffer charBuffer, ByteBuffer byteBuffer,
            CheckedExceptionBiConsumer<T, ByteBuffer, E> consumer) throws E {

        long sqlStringOffset = 0L;

        final int charBufferCapacity = charBuffer.capacity();

        for (;;) {

            charBuffer.clear();

            final long numCharactersRetrieved = writeToCharBuffer(charBuffer, sqlStringOffset);

            if (numCharactersRetrieved <= 0L) {

                break;
            }

            byteBuffer.clear();

            final boolean endOfInput = numCharactersRetrieved < charBufferCapacity;

            if (charsetEncoder.encode(charBuffer, byteBuffer, endOfInput).isError()) {

                throw new IllegalArgumentException();
            }

            consumer.accept(output, byteBuffer);

            sqlStringOffset += numCharactersRetrieved;
        }
    }
}
