package dev.jdata.db.utils.allocators;

import java.nio.CharBuffer;

public interface ICharBufferAllocator {

    CharBuffer allocateForDecodeBytes(long numBytes);

    void freeCharBuffer(CharBuffer charBuffer);
}
