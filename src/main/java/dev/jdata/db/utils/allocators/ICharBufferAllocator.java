package dev.jdata.db.utils.allocators;

import java.nio.CharBuffer;

public interface ICharBufferAllocator {

    CharBuffer allocateForEncodeCharacters(long numCharacters);

    CharBuffer allocateForDecodeBytes(long numBytes);

    void freeCharBuffer(CharBuffer charBuffer);
}
