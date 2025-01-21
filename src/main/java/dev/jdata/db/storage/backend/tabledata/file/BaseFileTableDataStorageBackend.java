package dev.jdata.db.storage.backend.tabledata.file;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.StorageDeleteRows;
import dev.jdata.db.storage.backend.StorageInsertRows;
import dev.jdata.db.storage.backend.ReadRows;
import dev.jdata.db.storage.backend.StorageUpdateRows;
import dev.jdata.db.storage.backend.tabledata.BaseTableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.NumStorageBitsGetter;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchema;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseFileTableDataStorageBackend extends BaseTableDataStorageBackend implements TableDataStorageBackend {

    protected abstract int getMaxLeftOverBytes();

    protected abstract void setDeleteMarker(byte[] outputRowBuffer, long outputRowBufferBitOffset);

    protected abstract StorageTableFileSchema adjustStorageTableFileSchema(StorageTableFileSchema currentStorageTableFileSchema, long rowId, long transactionId,
            RowDataNumBits rowDataNumBits);

    protected abstract void appendRowBits(FileTableStorageFile fileTableStorageFile, long rowId, long transactionId, RowDataNumBits rowDataNumBits, byte[] inputRowBuffer,
            long inputRowBufferBitOffset, byte[] outputRowBuffer, long outputRowBufferBitOffset) throws IOException;

    private final TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId;

    protected BaseFileTableDataStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsGetter numStorageBitsGetter,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId) {
        super(versionedDatabaseSchemas, numStorageBitsGetter);

        this.fileTableStorageByTableId = Objects.requireNonNull(fileTableStorageByTableId);
    }

    @Override
    public final void insertRows(int tableId, StorageInsertRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException {

        Checks.isTableId(tableId);
        Objects.requireNonNull(rows);
        Objects.requireNonNull(byteBufferAllocator);

        final FileTableStorageFiles fileTableStorageFiles = fileTableStorageByTableId.getTable(tableId);

        synchronized (fileTableStorageFiles) {

            final FileTableStorageFile currentFileTableStorageFile = fileTableStorageFiles.getCurrent();
            final StorageTableFileSchema currentStorageTableFileSchema = currentFileTableStorageFile.getStorageTableFileSchema();

            final int totalNumRowBytes = Math.max(currentStorageTableFileSchema.getTotalNumRowBytes(), rows.getMaxTotalNumRowBytes());

            final int outputRowBufferCapacity = (totalNumRowBytes * rows.getNumRows()) + getMaxLeftOverBytes();

            final byte[] outputRowBuffer = byteBufferAllocator.allocate(outputRowBufferCapacity);

            try {
                append(currentFileTableStorageFile, rows, outputRowBuffer);
            }
            finally {
                byteBufferAllocator.free(outputRowBuffer);
            }
        }
    }

    private void append(FileTableStorageFile initialFileTableStorageFile, StorageInsertRows rows, byte[] outputRowBuffer) throws IOException {

        long lastRowId = initialFileTableStorageFile.getLastRowId();

        FileTableStorageFile currentFileTableStorageFile = initialFileTableStorageFile;
        StorageTableFileSchema currentStorageTableFileSchema = initialFileTableStorageFile.getStorageTableFileSchema();

        long outputRowBufferBitOffset = 0;

        long numBitsAdded = 0L;
        int numRowsAdded = 0;

        final int numRows = rows.getNumRows();

        for (int i = 0; i < numRows; ++ i) {

            final long rowId = rows.getRowId(i);
            final long transactionId = rows.getTransactionId(i);
            final RowDataNumBits rowDataNumBits = rows.getRowDataNumBits(i);

            Checks.areEqual(rowDataNumBits.getNumColumns(), currentStorageTableFileSchema.getNumColumns());

            final StorageTableFileSchema adjustedStorageTableFileSchema = adjustStorageTableFileSchema(currentStorageTableFileSchema, rowId, transactionId, rowDataNumBits);

            final boolean startOnNewFile;

            if (adjustedStorageTableFileSchema != currentStorageTableFileSchema) {

                currentFileTableStorageFile = addFileTableStorageFile(currentFileTableStorageFile, adjustedStorageTableFileSchema, rowId);

                currentStorageTableFileSchema = adjustedStorageTableFileSchema;

                if (numRowsAdded > 0) {

                    final boolean rewindOneByte = currentFileTableStorageFile.computeNumLeftOverBits() != 0;

                    currentFileTableStorageFile.append(numRowsAdded, outputRowBuffer, BitBufferUtil.numBytes(numBitsAdded), rewindOneByte);

                    numBitsAdded = 0L;
                    numRowsAdded = 0;
                }

                startOnNewFile = true;
            }
            else {
                startOnNewFile = false;
            }

            if (!startOnNewFile) {

                final long rowIncrement = rowId - lastRowId;

                if (rowIncrement <= 0) {

                    throw new IllegalArgumentException();
                }
                else if (rowIncrement > 1) {

                    final long numRowsToClear = rowIncrement - 1;

                    for (int j = 0; j < numRowsToClear; ++ j) {

                        RowBitsUtil.clearRow(currentStorageTableFileSchema, rowId, transactionId, outputRowBuffer, outputRowBufferBitOffset);

                        outputRowBufferBitOffset += currentStorageTableFileSchema.getTotalNumRowBits();
                    }
                }
            }

            appendRowBits(currentFileTableStorageFile, rowId, transactionId, rowDataNumBits, rows.getRowBuffer(i), rows.getRowBufferBitOffset(i), outputRowBuffer,
                    outputRowBufferBitOffset);

            outputRowBufferBitOffset += currentStorageTableFileSchema.getTotalNumRowBits();

            lastRowId = rowId;
        }

        if (numRowsAdded > 0) {

            final boolean rewindOneByte = currentFileTableStorageFile.computeNumLeftOverBits() != 0;

            currentFileTableStorageFile.append(numRowsAdded, outputRowBuffer, BitBufferUtil.numBytes(numBitsAdded), rewindOneByte);
        }
    }

    @Override
    public final void updateRows(int tableId, StorageUpdateRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException {

        Checks.isTableId(tableId);
        Objects.requireNonNull(rows);
        Objects.requireNonNull(byteBufferAllocator);

        final FileTableStorageFiles fileTableStorageFiles = fileTableStorageByTableId.getTable(tableId);

        synchronized (fileTableStorageFiles) {

            final int outputRowBufferCapacity = rows.getMaxTotalNumRowBytes() + getMaxLeftOverBytes();

            final byte[] outputRowBuffer = byteBufferAllocator.allocate(outputRowBufferCapacity);

            try {
                updateRows(fileTableStorageFiles, rows, outputRowBuffer, byteBufferAllocator);
            }
            finally {
                byteBufferAllocator.free(outputRowBuffer);
            }
        }
    }

    private void updateRows(FileTableStorageFiles fileTableStorageFiles, StorageUpdateRows rows, byte[] outputRowBuffer, ByteBufferAllocator byteBufferAllocator) throws IOException {

        final int numRows = rows.getNumRows();

        for (int i = 0; i < numRows; ++ i) {

            final long rowId = rows.getRowId(i);
            final long transactionId = rows.getTransactionId(i);
            final RowDataNumBits rowDataNumBits = rows.getRowDataNumBits(i);

            FileTableStorageFile fileTableStorageFile = fileTableStorageFiles.findFile(rowId);

            StorageTableFileSchema storageTableFileSchema = fileTableStorageFile.getStorageTableFileSchema();

            Checks.areEqual(rowDataNumBits.getNumColumns(), storageTableFileSchema.getNumColumns());

            final StorageTableFileSchema adjustedStorageTableFileSchema = adjustStorageTableFileSchema(storageTableFileSchema, rowId, transactionId, rowDataNumBits);

            if (adjustedStorageTableFileSchema != storageTableFileSchema) {

                fileTableStorageFile = fileTableStorageFiles.updateFile(fileTableStorageFile, adjustedStorageTableFileSchema, byteBufferAllocator);

                storageTableFileSchema = adjustedStorageTableFileSchema;
            }

            updateRow(fileTableStorageFile, rows, numRows, outputRowBuffer);
        }
    }

    private void updateRow(FileTableStorageFile fileTableStorageFile, StorageUpdateRows rows, int rowIndex, byte[] outputRowBuffer) throws IOException {

        final long rowId = rows.getRowId(rowIndex);

        final StorageTableFileSchema storageTableFileSchema = fileTableStorageFile.getStorageTableFileSchema();

        final long fileRowBitOffset = fileTableStorageFile.getRowBitOffset(rowId);

        int bitOffsetWithinRow = retrieveOverlappingStartByte(fileTableStorageFile, fileRowBitOffset, outputRowBuffer);

        bitOffsetWithinRow += storageTableFileSchema.getNumRowIdBits() + storageTableFileSchema.getNumDeleteMarkerBits();

        final long transactionId = rows.getTransactionId(rowIndex);

        bitOffsetWithinRow += RowBitsUtil.addRowHeaderAfterDeleteMarker(storageTableFileSchema, transactionId, outputRowBuffer, bitOffsetWithinRow);

        final byte[] inputRowBuffer = rows.getRowBuffer(rowIndex);
        final long inputRowBufferBitOffset = rows.getRowBufferBitOffset(rowIndex);
        final RowDataNumBits rowDataNumBits = rows.getRowDataNumBits(rowIndex);

        bitOffsetWithinRow += RowBitsUtil.addRowData(storageTableFileSchema, inputRowBuffer, inputRowBufferBitOffset, outputRowBuffer, bitOffsetWithinRow, rowDataNumBits);

        mergeTrailingAndUpdate(fileTableStorageFile, outputRowBuffer, fileRowBitOffset, bitOffsetWithinRow);
    }

    @Override
    public final void deleteRows(int tableId, StorageDeleteRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException {

        Checks.isTableId(tableId);
        Objects.requireNonNull(rows);
        Objects.requireNonNull(byteBufferAllocator);

        final FileTableStorageFiles fileTableStorageFiles = fileTableStorageByTableId.getTable(tableId);

        synchronized (fileTableStorageFiles) {

            final int outputRowBufferCapacity = 100;

            final byte[] outputRowBuffer = byteBufferAllocator.allocate(outputRowBufferCapacity);

            try {
                deleteRows(fileTableStorageFiles, rows, outputRowBuffer, byteBufferAllocator);
            }
            finally {
                byteBufferAllocator.free(outputRowBuffer);
            }
        }
    }

    private void deleteRows(FileTableStorageFiles fileTableStorageFiles, StorageDeleteRows rows, byte[] outputRowBuffer, ByteBufferAllocator byteBufferAllocator) throws IOException {

        final int numRows = rows.getNumRows();

        for (int i = 0; i < numRows; ++ i) {

            final long rowId = rows.getRowId(i);

            final FileTableStorageFile fileTableStorageFile = fileTableStorageFiles.findFile(rowId);

            deleteRow(fileTableStorageFile, rows, numRows, outputRowBuffer);
        }
    }

    private void deleteRow(FileTableStorageFile fileTableStorageFile, StorageDeleteRows rows, int rowIndex, byte[] outputRowBuffer) throws IOException {

        final long rowId = rows.getRowId(rowIndex);

        final StorageTableFileSchema storageTableFileSchema = fileTableStorageFile.getStorageTableFileSchema();

        final long fileRowBitOffset = fileTableStorageFile.getRowBitOffset(rowId);

        int bitOffsetWithinRow = retrieveOverlappingStartByte(fileTableStorageFile, fileRowBitOffset, outputRowBuffer);

        final long deleteMarkerBitOffset = fileRowBitOffset + storageTableFileSchema.getNumRowIdBits();

        setDeleteMarker(outputRowBuffer, deleteMarkerBitOffset);

        bitOffsetWithinRow += storageTableFileSchema.getNumDeleteMarkerBits();

        mergeTrailingAndUpdate(fileTableStorageFile, outputRowBuffer, fileRowBitOffset, fileRowBitOffset + bitOffsetWithinRow);
    }

    private static int retrieveOverlappingStartByte(FileTableStorageFile fileTableStorageFile, long fileRowBitOffset, byte[] outputRowBuffer) throws IOException {

        int bitOffset = BitBufferUtil.numLeftoverBits(fileRowBitOffset);

        if (bitOffset != 0) {

            outputRowBuffer[0] = fileTableStorageFile.readByteAtRowBitOffset(fileRowBitOffset);
        }

        return bitOffset;
    }

    private static void mergeTrailingAndUpdate(FileTableStorageFile fileTableStorageFile, byte[] outputRowBuffer, long outputRowBufferStartBitOffset,
            long outputRowBufferEndBitOffset) throws IOException {

        final int numLeftoverEndBits = BitBufferUtil.numLeftoverBits(outputRowBufferEndBitOffset);

        if (numLeftoverEndBits != 0) {

            final byte existingFileEndByte = fileTableStorageFile.readByteAtRowBitOffset(outputRowBufferEndBitOffset);

            final int outputBufferEndByteOffset = Integers.checkUnsignedLongToUnsignedInt(outputRowBufferEndBitOffset / 8);

            outputRowBuffer[outputBufferEndByteOffset] = BitsUtil.merge(outputRowBuffer[outputBufferEndByteOffset], existingFileEndByte, numLeftoverEndBits);
        }

        final long fileByteOffset = Integers.checkUnsignedLongToUnsignedInt(outputRowBufferStartBitOffset / 8);

        final int numBytes = BitBufferUtil.numBytes(outputRowBufferStartBitOffset, outputRowBufferEndBitOffset);

        fileTableStorageFile.update(fileByteOffset, outputRowBuffer, numBytes);
    }

    @Override
    public final int readRows(int tableId, ReadRows readRows, byte[] dstBuffer) {

        throw new UnsupportedOperationException();
    }

    private FileTableStorageFile addFileTableStorageFile(FileTableStorageFile currentFileTableStorageFile, StorageTableFileSchema storageTableFileSchema, long rowId)
            throws IOException {

        Objects.requireNonNull(currentFileTableStorageFile);
        Objects.requireNonNull(storageTableFileSchema);
        Checks.isRowId(rowId);

        final StorageTableFileSchema currentStorageTableFileSchema = currentFileTableStorageFile.getStorageTableFileSchema();
        final StorageTableSchema currentStorageTableSchema = currentStorageTableFileSchema.getStorageTableSchema();

        if (!storageTableFileSchema.getStorageTableSchema().isSameTableAndSchemaVersion(currentStorageTableFileSchema.getStorageTableSchema())) {

            throw new IllegalArgumentException();
        }

        final FileTableStorageFiles fileTableStorageFiles = fileTableStorageByTableId.getTable(currentStorageTableSchema.getTableId());

        if (currentFileTableStorageFile != fileTableStorageFiles.getCurrent()) {

            throw new IllegalArgumentException();
        }

        if (currentFileTableStorageFile.getLastRowId() != rowId) {

            throw new IllegalArgumentException();
        }

        return fileTableStorageFiles.addFile(storageTableFileSchema, rowId);
    }
}
