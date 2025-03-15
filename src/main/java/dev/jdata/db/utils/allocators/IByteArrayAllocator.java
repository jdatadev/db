package dev.jdata.db.utils.allocators;

public interface IByteArrayAllocator {

    byte[] allocateByteArray(int capacity);

    void freeByteArray(byte[] buffer);
}
