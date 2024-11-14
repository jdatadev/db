package dev.jdata.db.storage.backend;

public interface DataAccess extends DataStorer {

    int readRows(int tableId, ReadRows rows, byte[] dstBuffer);
}
