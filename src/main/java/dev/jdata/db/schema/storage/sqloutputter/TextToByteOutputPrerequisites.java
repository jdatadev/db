package dev.jdata.db.schema.storage.sqloutputter;

import java.nio.charset.CharsetEncoder;
import java.util.Objects;

import dev.jdata.db.utils.jdk.niobuffers.ByteArrayByteBufferAllocator;
import dev.jdata.db.utils.jdk.niobuffers.CharBufferAllocator;

public final class TextToByteOutputPrerequisites {

    private final CharsetEncoder charsetEncoder;
    private final CharBufferAllocator charBufferAllocator;
    private final ByteArrayByteBufferAllocator byteBufferAllocator;

    public TextToByteOutputPrerequisites(CharsetEncoder charsetEncoder, CharBufferAllocator charBufferAllocator, ByteArrayByteBufferAllocator byteBufferAllocator) {

        this.charsetEncoder = Objects.requireNonNull(charsetEncoder);
        this.charBufferAllocator = Objects.requireNonNull(charBufferAllocator);
        this.byteBufferAllocator = Objects.requireNonNull(byteBufferAllocator);
    }

    public CharsetEncoder getCharsetEncoder() {
        return charsetEncoder;
    }

    public CharBufferAllocator getCharBufferAllocator() {
        return charBufferAllocator;
    }

    public ByteArrayByteBufferAllocator getByteBufferAllocator() {
        return byteBufferAllocator;
    }
}
