package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.CharBuffer;

public interface ICharBufferAllocator {

    CharBuffer allocateForEncodeCharacters(long numCharacters);

    CharBuffer allocateForDecodeBytes(long numBytes);

    void freeCharBuffer(CharBuffer charBuffer);
}
