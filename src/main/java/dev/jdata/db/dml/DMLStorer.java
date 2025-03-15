package dev.jdata.db.dml;

import dev.jdata.db.utils.allocators.IByteArrayAllocator;

@Deprecated // currently not in use
public interface DMLStorer {

    void insertRows(int tableId, DMLInsertRows rows, IByteArrayAllocator byteBufferAllocator);

    void updateRows(int tableId, DMLUpdateRows rows, IByteArrayAllocator byteBufferAllocator);

//    void deleteRows(int tableId, StorageDeleteRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException;
}
