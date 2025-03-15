package dev.jdata.db.engine.server.network.protocol.strings;

import java.nio.CharBuffer;
import java.util.Objects;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.checks.Checks;

public final class MutableString extends CharString implements IClearable {

    public interface CharArrayAllocator {

        char[] allocateCharArray(int length);

        void freeCharArray(char[] array);
    }

    @Override
    public void clear() {

        this.numCharacters = 0;
    }

    public void append(char c, CharArrayAllocator charArrayAllocator) {

        final char[] dst = checkCapacity(c, charArrayAllocator, (a, l) -> a != null ? a.length << 1 : 10);

        dst[numCharacters ++] = c;
    }

    public void set(CharBuffer charBuffer, int offset, int length, CharArrayAllocator allocator) {

        Checks.isExactlyZero(charBuffer.position());
        Objects.checkFromIndexSize(offset, charBuffer.limit(), length);
        Objects.requireNonNull(allocator);

        final char[] dst = checkCapacity(length, allocator, (a, l) -> l);

        for (int i = 0; i < length; ++ i) {

            dst[i] = charBuffer.charAt(offset + i);
        }

        this.numCharacters = length;
    }

    @FunctionalInterface
    private interface CapacityGetter {

        int getCapacity(char[] array, int length);
    }

    private char[] checkCapacity(int length, CharArrayAllocator allocator, CapacityGetter capacityGetter) {

        final char[] dst;

        if (charArray == null) {

            dst = this.charArray = allocator.allocateCharArray(capacityGetter.getCapacity(charArray, length));
        }
        else if (length > charArray.length) {

            allocator.freeCharArray(charArray);

            dst = this.charArray = allocator.allocateCharArray(capacityGetter.getCapacity(charArray, length));
        }
        else {
            dst = charArray;
        }

        return dst;
    }
}
