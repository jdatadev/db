package dev.jdata.db.engine.server.network;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;

import dev.jdata.db.engine.database.operations.IDatabaseExecuteOperations.IDataWriter;
import dev.jdata.db.utils.checks.Checks;

class ByteBufferDataWriter implements IDataWriter<RuntimeException> {

    private ByteBuffer byteBuffer;
    private CharsetEncoder charsetEncoder;
    private CharBuffer charBuffer;

    void initialize(ByteBuffer byteBuffer, CharsetEncoder charsetEncoder, CharBuffer charBuffer) {

        this.byteBuffer = Objects.requireNonNull(byteBuffer);
        this.charsetEncoder = Objects.requireNonNull(charsetEncoder);
        this.charBuffer = Checks.isEmpty(charBuffer);
    }

    @Override
    public final void writeByte(byte b) {

        byteBuffer.put(b);
    }

    @Override
    public final void writeShort(short s) {

        byteBuffer.putShort(s);
    }

    @Override
    public final void writeInt(int i) {

        byteBuffer.putInt(i);
    }

    @Override
    public final void writeLong(long l) {

        byteBuffer.putLong(l);
    }

    @Override
    public final void writeString(CharSequence charSequence) {

        charBuffer.append(charSequence);

        charsetEncoder.encode(charBuffer, byteBuffer, true);
    }
}
