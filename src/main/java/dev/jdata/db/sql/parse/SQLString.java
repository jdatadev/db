package dev.jdata.db.sql.parse;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;

import dev.jdata.db.utils.function.CheckedExceptionBiConsumer;

public interface SQLString {

    public static <T, E extends Exception> void writeSQLString(SQLString sqlString, T output, CharsetEncoder charsetEncoder, CharBuffer charBuffer, ByteBuffer byteBuffer,
            CheckedExceptionBiConsumer<T, ByteBuffer, E> consumer) throws E {

        long sqlStringOffset = 0L;

        final int charBufferCapacity = charBuffer.capacity();

        for (;;) {

            charBuffer.clear();

            final long numCharactersRetrieved = sqlString.writeToCharBuffer(charBuffer, sqlStringOffset);

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

    void append(Appendable appendable);

    long getLength();

    void write(DataOutput dataOutput) throws IOException;

    long writeToCharBuffer(CharBuffer dst, long offset);

    default String asString() {

        final StringBuilder sb = new StringBuilder(10000);

        append(sb);

        return sb.toString();
    }

    default <T, E extends Exception> void writeSQLString(T output, CharsetEncoder charsetEncoder, CharBuffer charBuffer, ByteBuffer byteBuffer,
            CheckedExceptionBiConsumer<T, ByteBuffer, E> consumer) throws E {

        writeSQLString(this, output, charsetEncoder, charBuffer, byteBuffer, consumer);
    }
}
