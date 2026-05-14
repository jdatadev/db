package dev.jdata.db.schema.storage.sqloutputter;

import java.nio.charset.CharsetEncoder;
import java.util.Objects;

import dev.jdata.db.utils.jdk.niobuffers.CachedByteArrayByteBufferAllocator;
import dev.jdata.db.utils.jdk.niobuffers.CharBufferAllocator;

public final class TextToByteOutputPrerequisites {

    private final CharsetEncoder charsetEncoder;
    private final CharBufferAllocator charBufferAllocator;
    private final CachedByteArrayByteBufferAllocator byteBufferAllocator;

    public TextToByteOutputPrerequisites(CharsetEncoder charsetEncoder, CharBufferAllocator charBufferAllocator, CachedByteArrayByteBufferAllocator byteBufferAllocator) {

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

    public CachedByteArrayByteBufferAllocator getByteBufferAllocator() {
        return byteBufferAllocator;
    }
}
