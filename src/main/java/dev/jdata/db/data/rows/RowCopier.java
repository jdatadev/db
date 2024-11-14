package dev.jdata.db.data.rows;

import dev.jdata.db.storage.backend.StorageDataType;

@Deprecated
public interface RowCopier {

//    DBType getType(byte[] rowBuffer, int offset, int length);

//    int getInteger(byte[] rowBuffer, int offset, int length);

/*
    private static final class BufferPos {

    }
*/

    void copy(StorageDataType storageDataType, byte[] inputRowBuffer, int inputRowBufferByteOffset, int inputRowBufferBitOffset, byte[] outputRowBuffer,
            int outputRowBufferByteOffset, int outputRowBufferBitOffset, int numBits);
}
