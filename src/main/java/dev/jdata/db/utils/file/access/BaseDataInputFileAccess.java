package dev.jdata.db.utils.file.access;

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.util.Objects;

abstract class BaseDataInputFileAccess<T extends Closeable & DataInput> extends BaseFileAccess implements DataInput {

    private final T file;

    BaseDataInputFileAccess(T file) {

        this.file = Objects.requireNonNull(file);
    }

    @Override
    public final void close() throws IOException {

        file.close();
    }

    @Override
    public final void readFully(byte[] b) throws IOException {

        file.readFully(b);
    }

    @Override
    public final void readFully(byte[] b, int off, int len) throws IOException {

        file.readFully(b, off, len);
    }

    @Override
    public final int skipBytes(int n) throws IOException {

        return file.skipBytes(n);
    }

    @Override
    public final boolean readBoolean() throws IOException {

        return file.readBoolean();
    }

    @Override
    public final byte readByte() throws IOException {

        return file.readByte();
    }

    @Override
    public final int readUnsignedByte() throws IOException {

        return file.readUnsignedByte();
    }

    @Override
    public final short readShort() throws IOException {

        return file.readShort();
    }

    @Override
    public final int readUnsignedShort() throws IOException {

        return file.readUnsignedShort();
    }

    @Override
    public final char readChar() throws IOException {

        return file.readChar();
    }

    @Override
    public final int readInt() throws IOException {

        return file.readInt();
    }

    @Override
    public final long readLong() throws IOException {

        return file.readLong();
    }

    @Override
    public final float readFloat() throws IOException {

        return file.readFloat();
    }

    @Override
    public final double readDouble() throws IOException {

        return file.readDouble();
    }

    @Override
    public final String readLine() throws IOException {

        return file.readLine();
    }

    @Override
    public final String readUTF() throws IOException {

        return file.readUTF();
    }

    final T getFile() {

        return file;
    }
}
