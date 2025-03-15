package dev.jdata.db.storage.backend;

import java.io.IOException;

import dev.jdata.db.utils.allocators.IByteArrayAllocator;

public interface DataStorer {

    void insertRows(int tableId, StorageInsertRows rows, IByteArrayAllocator byteBufferAllocator) throws IOException;

    void updateRows(int tableId, StorageUpdateRows rows, IByteArrayAllocator byteBufferAllocator) throws IOException;

    void deleteRows(int tableId, StorageDeleteRows rows, IByteArrayAllocator byteBufferAllocator) throws IOException;
}
