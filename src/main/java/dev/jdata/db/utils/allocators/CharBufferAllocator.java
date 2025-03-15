package dev.jdata.db.utils.allocators;

import java.nio.CharBuffer;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class CharBufferAllocator extends BaseBufferAllocator<CharBuffer> implements ICharBufferAllocator {

    public CharBufferAllocator() {
        super(CharBuffer::allocate);
    }

    @Override
    public CharBuffer allocateForDecodeBytes(long numBytes) {

        Checks.isNumBytes(numBytes);

        return allocateArrayInstance(Integers.checkUnsignedLongToUnsignedInt(numBytes));
    }

    @Override
    public void freeCharBuffer(CharBuffer charBuffer) {

        freeArrayInstance(charBuffer);
    }
}
