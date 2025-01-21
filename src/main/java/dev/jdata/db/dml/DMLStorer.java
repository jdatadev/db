package dev.jdata.db.dml;

import dev.jdata.db.storage.backend.tabledata.file.ByteBufferAllocator;

@Deprecated // currently not in use
public interface DMLStorer {

    void insertRows(int tableId, DMLInsertRows rows, ByteBufferAllocator byteBufferAllocator);

    void updateRows(int tableId, DMLUpdateRows rows, ByteBufferAllocator byteBufferAllocator);

//    void deleteRows(int tableId, StorageDeleteRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException;
}
