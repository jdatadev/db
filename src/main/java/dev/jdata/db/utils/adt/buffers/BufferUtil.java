package dev.jdata.db.utils.adt.buffers;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Objects;

import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.elements.ByIndex.ByIndexStringAdder;
import dev.jdata.db.utils.checks.Checks;

public class BufferUtil {

    public static String toString(ByteBuffer byteBuffer) {

        Objects.requireNonNull(byteBuffer);

        return toString(byteBuffer, byteBuffer.position(), byteBuffer.remaining());
    }

    public static String toString(ByteBuffer byteBuffer, int offset, int length) {

        Objects.requireNonNull(byteBuffer);
        Checks.isOffset(offset);
        Checks.isLengthAboveOrAtZero(length);

        final StringBuilder sb = new StringBuilder(length);

        toString(byteBuffer, offset, length, sb);

        return sb.toString();
    }

    public static void toString(ByteBuffer byteBuffer, int offset, int length, StringBuilder sb) {

        Objects.requireNonNull(byteBuffer);
        Checks.isOffset(offset);
        Checks.isLengthAboveOrAtZero(length);

        toString(byteBuffer, offset, length, 1, sb, (b, i, s) -> s.append(b.get((int)i)));
    }

    private static <T extends Buffer> String toString(T buffer, int numCharactersPerElement, ByIndexStringAdder<T> stringAdder) {

        return toString(buffer, buffer.position(), buffer.remaining(), numCharactersPerElement, stringAdder);
    }

    private static <T extends Buffer> String toString(T buffer, int offset, int length, int numCharactersPerElement, ByIndexStringAdder<T> stringAdder) {

        final StringBuilder sb = new StringBuilder(length * numCharactersPerElement);

        return sb.toString();
    }

    private static <T extends Buffer> void toString(T buffer, int offset, int length, int numCharactersPerElement, StringBuilder sb, ByIndexStringAdder<T> stringAdder) {

        ByIndex.closureOrConstantToString(buffer, offset, length, sb, null, null, stringAdder);
    }
}
