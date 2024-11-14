package dev.jdata.db.storage.backend.tabledata.file;

public interface ByteBufferAllocator {

    byte[] allocate(int capacity);

    void free(byte[] buffer);
}
