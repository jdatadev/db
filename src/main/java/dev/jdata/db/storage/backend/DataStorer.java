package dev.jdata.db.storage.backend;

import java.io.IOException;

import dev.jdata.db.storage.backend.tabledata.file.ByteBufferAllocator;

public interface DataStorer {

    void insertRows(int tableId, InsertRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException;

    void updateRows(int tableId, UpdateRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException;

    void deleteRows(int tableId, DeleteRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException;
}
