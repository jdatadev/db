package dev.jdata.db.utils.file.access;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

abstract class BaseDataInputOutputFileAccess<T extends Closeable & DataInput & DataOutput> extends BaseDataInputFileAccess<T> implements DataOutput {

    BaseDataInputOutputFileAccess(T file) {
        super(file);
    }

    @Override
    public final void write(int b) throws IOException {

        getFile().write(b);
    }

    @Override
    public final void write(byte[] b) throws IOException {

        getFile().write(b);
    }

    @Override
    public final void write(byte[] b, int off, int len) throws IOException {

        getFile().write(b, off, len);
    }

    @Override
    public final void writeBoolean(boolean v) throws IOException {

        getFile().writeBoolean(v);
    }

    @Override
    public final void writeByte(int v) throws IOException {

        getFile().writeByte(v);
    }

    @Override
    public final void writeShort(int v) throws IOException {

        getFile().writeShort(v);
    }

    @Override
    public final void writeChar(int v) throws IOException {

        getFile().writeChar(v);
    }

    @Override
    public final void writeInt(int v) throws IOException {

        getFile().writeInt(v);
    }

    @Override
    public final void writeLong(long v) throws IOException {

        getFile().writeLong(v);
    }

    @Override
    public final void writeFloat(float v) throws IOException {

        getFile().writeFloat(v);
    }

    @Override
    public final void writeDouble(double v) throws IOException {

        getFile().writeDouble(v);
    }

    @Override
    public final void writeBytes(String s) throws IOException {

        getFile().writeBytes(s);
    }

    @Override
    public final void writeChars(String s) throws IOException {

        getFile().writeChars(s);
    }

    @Override
    public final void writeUTF(String s) throws IOException {

        getFile().writeUTF(s);
    }
}
