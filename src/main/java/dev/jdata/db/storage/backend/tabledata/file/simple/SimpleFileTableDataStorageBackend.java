package dev.jdata.db.storage.backend.tabledata.file.simple;

import java.io.IOException;

import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.tabledata.file.BaseFileTableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.file.FileTableStorageFile;
import dev.jdata.db.storage.backend.tabledata.file.FileTableStorageFiles;
import dev.jdata.db.storage.backend.tabledata.file.RowBitsUtil;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema;
import dev.jdata.db.utils.bits.BitBufferUtil;

final class SimpleFileTableDataStorageBackend extends BaseFileTableDataStorageBackend {

    SimpleFileTableDataStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsGetter numStorageBitsGetter,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId) {
        super(versionedDatabaseSchemas, numStorageBitsGetter, fileTableStorageByTableId);
    }

    @Override
    protected int getMaxLeftOverBytes() {

        return 0;
    }

    @Override
    protected StorageTableFileSchema adjustStorageTableFileSchema(StorageTableFileSchema currentStorageTableFileSchema, long rowId, long transactionId, RowDataNumBits rowDataNumBits) {

        return currentStorageTableFileSchema;
    }

    @Override
    protected void appendRowBits(FileTableStorageFile fileTableStorageFile, long rowId, long transactionId, RowDataNumBits rowDataNumBits, byte[] inputRowBuffer,
            long inputRowBufferBitOffset, byte[] outputRowBuffer, long outputRowBufferBitOffset) throws IOException {

        if (fileTableStorageFile.hasLeftOverBits()) {

            throw new IllegalArgumentException();
        }

        final StorageTableFileSchema storageTableFileSchema = fileTableStorageFile.getStorageTableFileSchema();

        final int totalNumRowBits = storageTableFileSchema.getStorageTableSchema().getTotalMaxBits();

        final int currentFileNumBits = storageTableFileSchema.getTotalNumRowBits();

        if (totalNumRowBits != currentFileNumBits) {

            throw new IllegalArgumentException();
        }

        final int totalNumRowBytes = BitBufferUtil.numBytesExact(totalNumRowBits);

        final int numBitsAdded = RowBitsUtil.addRowData(storageTableFileSchema, outputRowBuffer, outputRowBufferBitOffset, outputRowBuffer, 0, rowDataNumBits);

        final int numBytesAdded = BitBufferUtil.numBytesExact(numBitsAdded);

        fileTableStorageFile.append(1, outputRowBuffer, numBytesAdded, false);
    }

    @Override
    protected void setDeleteMarker(byte[] outputRowBuffer, long outputRowBufferBitOffset) {

        if (!BitBufferUtil.isBitOffsetOnByteBoundary(outputRowBufferBitOffset)) {

            throw new IllegalArgumentException();
        }

        BitBufferUtil.setBits(outputRowBuffer, true, outputRowBufferBitOffset, 8);
    }
}
