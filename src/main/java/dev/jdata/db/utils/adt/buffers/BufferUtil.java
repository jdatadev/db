package dev.jdata.db.utils.adt.buffers;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.ByIndex;
import dev.jdata.db.utils.adt.byindex.ByIndex.ByIndexStringAdder;
import dev.jdata.db.utils.checks.Checks;

public class BufferUtil {

    public static String toString(ByteBuffer byteBuffer) {

        Objects.requireNonNull(byteBuffer);

        return toString(byteBuffer, byteBuffer.position(), byteBuffer.remaining());
    }

    public static String toString(ByteBuffer byteBuffer, int offset, int length) {

        Objects.requireNonNull(byteBuffer);
        Checks.isIntOffset(offset);
        Checks.isIntLengthAboveOrAtZero(length);

        return toString(byteBuffer, offset, length, 3, (b, i, s) -> s.append(b.get((int)i)));
    }

    public static void toString(ByteBuffer byteBuffer, int offset, int length, StringBuilder sb) {

        Objects.requireNonNull(byteBuffer);
        Checks.isIntOffset(offset);
        Checks.isIntLengthAboveOrAtZero(length);

        toString(byteBuffer, offset, length, 3, sb, (b, i, s) -> s.append(b.get((int)i)));
    }

    private static <T extends Buffer> String toString(T buffer, int offset, int length, int numCharactersPerElement, ByIndexStringAdder<T> stringAdder) {

        final StringBuilder sb = new StringBuilder(length * numCharactersPerElement);

        toString(buffer, offset, length, sb, stringAdder);

        return sb.toString();
    }

    private static <T extends Buffer> String toString(T buffer, int offset, int length, int numCharactersPerElement, StringBuilder sb, ByIndexStringAdder<T> stringAdder) {

        sb.ensureCapacity(sb.length() + length * numCharactersPerElement);

        toString(buffer, offset, length, sb, stringAdder);

        return sb.toString();
    }

    private static <T extends Buffer> void toString(T buffer, int offset, int length, StringBuilder sb, ByIndexStringAdder<T> stringAdder) {

        ByIndex.closureOrConstantToString(buffer, offset, length, sb, null, null, stringAdder);
    }
}
