package dev.jdata.db.data.cache;

import java.io.DataOutput;

import dev.jdata.db.data.RowDataNumBitsGetter;

public interface DataCache {

    boolean writeRowToDataOutput(int tableId, long rowId, DataOutput dataOutput, int[] columnIndices, RowDataNumBitsGetter outputRowDataNumBitsGetter, boolean updated);

    boolean retrieveRow(int tableId, long rowId, byte[] outputBuffer, long outputBufferStartBitOffset, int[] columnIndices, RowDataNumBitsGetter outputRowDataNumBitsGetter,
            boolean updated);
}
