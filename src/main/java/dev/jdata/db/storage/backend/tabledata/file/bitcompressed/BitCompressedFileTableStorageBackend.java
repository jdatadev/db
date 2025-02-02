package dev.jdata.db.storage.backend.tabledata.file.bitcompressed;

import java.io.IOException;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.tabledata.NumStorageBitsGetter;
import dev.jdata.db.storage.backend.tabledata.file.BaseFileTableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.file.FileTableStorageFile;
import dev.jdata.db.storage.backend.tabledata.file.FileTableStorageFiles;
import dev.jdata.db.storage.backend.tabledata.file.RowBitsUtil;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.bits.BitsUtil;

final class BitCompressedFileTableStorageBackend extends BaseFileTableDataStorageBackend {

    BitCompressedFileTableStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsGetter numStorageBitsGetter,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId) {
        super(versionedDatabaseSchemas, numStorageBitsGetter, fileTableStorageByTableId);
    }

    @Override
    protected int getMaxLeftOverBytes() {

        return 2;
    }

    @Override
    protected StorageTableFileSchema adjustStorageTableFileSchema(StorageTableFileSchema currentStorageTableFileSchema, long rowId, long transactionId,
            RowDataNumBits rowDataNumBits) {

        final int numInputRowDataBits = rowDataNumBits.getTotalNumRowDataBits();

        final int currentFileNumBits = currentStorageTableFileSchema.getTotalNumRowBits();

        final int numInputRowIdBits = BitsUtil.getNumStorageBits(rowId, false);
        final int numInputTransactionIdBits = BitsUtil.getNumStorageBits(transactionId, false);

        final int totalNumInputRowBits = numInputRowIdBits + numInputTransactionIdBits + currentStorageTableFileSchema.getNumNullValueBitMapBits() + numInputRowDataBits;

        final StorageTableFileSchema storageTableFileSchema;

        if (totalNumInputRowBits < currentFileNumBits) {

            throw new IllegalArgumentException();
        }
        else if (totalNumInputRowBits > currentFileNumBits) {

            storageTableFileSchema = currentStorageTableFileSchema.adjustNumBits(numInputRowIdBits, 1, numInputTransactionIdBits, totalNumInputRowBits);
        }
        else {
            // Same number of rows per file
            storageTableFileSchema = currentStorageTableFileSchema;
        }

        return storageTableFileSchema;
    }

    @Override
    protected void appendRowBits(FileTableStorageFile fileTableStorageFile, long rowId, long transactionId, RowDataNumBits rowDataNumBits, byte[] inputRowBuffer,
            long inputRowBufferBitOffset, byte[] outputRowBuffer, long outputBufferBitOffset) throws IOException {

        final StorageTableFileSchema storageTableFileSchema = fileTableStorageFile.getStorageTableFileSchema();

        final int numLeftOverBits = fileTableStorageFile.computeNumLeftOverBits();

        final boolean hasLeftOverBits = numLeftOverBits != 0;

        final byte lastByte;
        final int numExistingBits;

        if (hasLeftOverBits) {

            lastByte = fileTableStorageFile.readLastByte();
            numExistingBits = 8 - numLeftOverBits;
        }
        else {
            lastByte = 0;
            numExistingBits = 0;
        }

        final int numBitsAdded = RowBitsUtil.addRowData(storageTableFileSchema, inputRowBuffer, inputRowBufferBitOffset, outputRowBuffer, numExistingBits, rowDataNumBits);

        if (hasLeftOverBits) {

            final byte mask = (byte)(BitsUtil.mask(numExistingBits) << numLeftOverBits);

            outputRowBuffer[0] |= lastByte & mask;
        }
    }

    @Override
    protected void setDeleteMarker(byte[] outputRowBuffer, long outputRowBufferBitOffset) {

        BitBufferUtil.setBitValue(outputRowBuffer, true, outputRowBufferBitOffset);
    }
}
