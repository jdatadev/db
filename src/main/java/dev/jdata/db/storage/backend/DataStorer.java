package dev.jdata.db.storage.backend;

import java.io.IOException;

import dev.jdata.db.storage.backend.tabledata.file.ByteBufferAllocator;

public interface DataStorer {

    void insertRows(int tableId, StorageInsertRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException;

    void updateRows(int tableId, StorageUpdateRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException;

    void deleteRows(int tableId, StorageDeleteRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException;
}
