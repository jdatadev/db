package dev.jdata.db.engine.database.strings;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.CharBuffer;

import dev.jdata.db.schema.storage.sqloutputter.IExceptionAppendable;

public interface IStringWriter {

    <P, E extends Exception> void append(long stringRef, P parameter, IExceptionAppendable<P, E> appendable) throws E;

    @Deprecated // necessary?
    void write(long stringRef, DataOutput dataOutput) throws IOException;

    @Deprecated // necessary?
    void writeToCharBuffer(long stringRef, CharBuffer charBuffer, int dstOffset);

    default void append(long stringRef, Appendable appendable) throws IOException {

        append(stringRef, appendable, (c, a) -> a.append(c));
    }
}
