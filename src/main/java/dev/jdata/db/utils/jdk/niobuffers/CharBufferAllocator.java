package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.CharBuffer;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class CharBufferAllocator extends BaseBufferAllocator<CharBuffer> implements ICharBufferAllocator {

    public CharBufferAllocator() {
        super(CharBuffer::allocate);
    }

    @Override
    public CharBuffer allocateForEncodeCharacters(long numCharacters) {

        Checks.isNumCharacters(numCharacters);

        return allocateFromFreeListOrCreateCapacityInstance(Integers.checkUnsignedLongToUnsignedInt(numCharacters));
    }

    @Override
    public CharBuffer allocateForDecodeBytes(long numBytes) {

        Checks.isNumBytes(numBytes);

        return allocateFromFreeListOrCreateCapacityInstance(Integers.checkUnsignedLongToUnsignedInt(numBytes));
    }

    @Override
    public void freeCharBuffer(CharBuffer charBuffer) {

        freeArrayInstance(charBuffer);
    }
}
