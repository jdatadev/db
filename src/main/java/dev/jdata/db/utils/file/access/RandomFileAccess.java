package dev.jdata.db.utils.file.access;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface RandomFileAccess extends FileAccess, DataInput, DataOutput {

    long getFilePointer() throws IOException;
    void seek(long offset) throws IOException;

    long length() throws IOException;

    int read(byte[] buffer) throws IOException;
}
