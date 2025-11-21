package dev.jdata.db.utils.adt.arrays;

public interface IByteArrayAllocator {

    byte[] allocateByteArray(int capacity);

    void freeByteArray(byte[] buffer);
}
