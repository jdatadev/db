package dev.jdata.db.storage.backend.transactionlog.backend.file;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.storage.backend.StorageDMLRows;
import dev.jdata.db.storage.backend.StorageDeleteRows;
import dev.jdata.db.storage.backend.StorageInsertRows;
import dev.jdata.db.storage.backend.StorageInsertUpdateRows;
import dev.jdata.db.storage.backend.StorageUpdateRows;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchema;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchema.StorageSchemaColumn;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.tabledata.file.ByteBufferAllocator;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseFilesTransactionLogBackend extends BaseFileTransactionLogBackend {

    private static final int BUFFER_SIZE = 10 * 1000 * 1000;

    private static final int NUM_INT_BITS = Integer.SIZE;
    private static final int NUM_LONG_BITS = Long.SIZE;

    /*
    private final Path rootPath;

    BaseFilesTransactionLogBackend(Path rootPath) {

        this.rootPath = Objects.requireNonNull(rootPath);
    }
*/
    private final StorageTableSchemas storageTableSchemas;
    private final FileTransactionLogFiles transactionLogFiles;

    protected BaseFilesTransactionLogBackend(StorageTableSchemas storageTableSchemas, FileTransactionLogFiles transactionLogFiles) {

        this.storageTableSchemas = Objects.requireNonNull(storageTableSchemas);
        this.transactionLogFiles = Objects.requireNonNull(transactionLogFiles);
    }

    @Override
    public final void startTransaction(long transactionId) throws IOException {

        final FileTransactionLogFile fileTransactionLogFile = transactionLogFiles.addFile(transactionId);

        fileTransactionLogFile.writeStartTransaction(transactionId);
    }

    @Override
    public final void commitTransaction(long transactionId) throws IOException {

        final FileTransactionLogFile fileTransactionLogFile = transactionLogFiles.getFile(transactionId);

        fileTransactionLogFile.writeCommitTransaction(transactionId);
    }

    private StorageTableSchema getStorageTableSchema(int tableId, StorageDMLRows<?> dmlRows, int rowIndex) {

        final DatabaseSchemaVersion databaseSchemaVersion = dmlRows.getDatabaseSchemaVersion(rowIndex);

        return storageTableSchemas.getStorageTableSchema(tableId, databaseSchemaVersion);
    }

    @Override
    public final void insertRows(int tableId, StorageInsertRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException {

        Checks.isTableId(tableId);
        Objects.requireNonNull(rows);
        Objects.requireNonNull(byteBufferAllocator);

        insertOrUpdateRows(tableId, TransactionOperationCode.INSERT, rows, byteBufferAllocator);
    }

    @Override
    public final void updateRows(int tableId, StorageUpdateRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException {

        Checks.isTableId(tableId);
        Objects.requireNonNull(rows);
        Objects.requireNonNull(byteBufferAllocator);

        insertOrUpdateRows(tableId, TransactionOperationCode.UPDATE, rows, byteBufferAllocator);
    }

    private void insertOrUpdateRows(int tableId, TransactionOperationCode operationCode, StorageInsertUpdateRows<?> rows, ByteBufferAllocator byteBufferAllocator) throws IOException {

        final byte[] outputBuffer = byteBufferAllocator.allocate(BUFFER_SIZE);

        try {
            final int numRows = rows.getNumRows();

            final StorageTableSchema storageTableSchema = getStorageTableSchema(tableId, rows, numRows);

            long outputBufferBitOffset = 0;

            BitBufferUtil.setIntValue(outputBuffer, tableId, false, outputBufferBitOffset, NUM_INT_BITS);

            outputBufferBitOffset += NUM_INT_BITS;

            for (int i = 0; i < numRows; ++ i) {

                final int numColumns = storageTableSchema.getNumColumns();

                final RowDataNumBits rowDataNumBits = rows.getRowDataNumBits(i);

                if (numColumns != rowDataNumBits.getNumColumns()) {

                    throw new IllegalStateException();
                }

                BitBufferUtil.setLongValue(outputBuffer, rows.getRowId(i), false, outputBufferBitOffset, NUM_LONG_BITS);

                outputBufferBitOffset += NUM_LONG_BITS;

                final long transactionId = rows.getTransactionId(i);

                BitBufferUtil.setLongValue(outputBuffer, transactionId, false, outputBufferBitOffset, NUM_LONG_BITS);

                outputBufferBitOffset += NUM_LONG_BITS;

                for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

                    final byte[] inputBuffer = rows.getRowBuffer(columnIndex);
                    final long inputBufferBitOffset = rows.getRowBufferBitOffset(columnIndex);
                    final int numInputBits = rowDataNumBits.getNumBits(columnIndex);

                    final StorageSchemaColumn storageSchemaColumn = storageTableSchema.getColumn(columnIndex);;

                    final int numOutputBits = storageSchemaColumn.getMaxBits();

                    BitBufferUtil.copyBits(inputBuffer, inputBufferBitOffset, numInputBits, outputBuffer, outputBufferBitOffset, numOutputBits);

                    outputBufferBitOffset += numOutputBits;
                }

                if (!BitBufferUtil.isBitOffsetOnByteBoundary(outputBufferBitOffset)) {

                    throw new IllegalStateException();
                }

                transactionLogFiles.getFile(transactionId).append(transactionId, operationCode, outputBuffer, numColumns);;
            }
        }
        finally {

            byteBufferAllocator.free(outputBuffer);
        }
    }

    @Override
    public final void deleteRows(int tableId, StorageDeleteRows rows, ByteBufferAllocator byteBufferAllocator) throws IOException {

        Checks.isTableId(tableId);
        Objects.requireNonNull(rows);
        Objects.requireNonNull(byteBufferAllocator);

        final byte[] outputBuffer = byteBufferAllocator.allocate(BUFFER_SIZE);

        try {
            final int numRows = rows.getNumRows();

            final StorageTableSchema storageTableSchema = getStorageTableSchema(tableId, rows, numRows);

            long outputBufferBitOffset = 0;

            BitBufferUtil.setIntValue(outputBuffer, tableId, false, outputBufferBitOffset, NUM_INT_BITS);

            outputBufferBitOffset += NUM_INT_BITS;

            for (int i = 0; i < numRows; ++ i) {

                final int numColumns = storageTableSchema.getNumColumns();

                BitBufferUtil.setLongValue(outputBuffer, rows.getRowId(i), false, outputBufferBitOffset, NUM_LONG_BITS);

                outputBufferBitOffset += NUM_LONG_BITS;

                final long transactionId = rows.getTransactionId(i);

                BitBufferUtil.setLongValue(outputBuffer, transactionId, false, outputBufferBitOffset, NUM_LONG_BITS);

                outputBufferBitOffset += NUM_LONG_BITS;


                transactionLogFiles.getFile(transactionId).append(transactionId, TransactionOperationCode.DELETE, outputBuffer, numColumns);;
            }
        }
        finally {

            byteBufferAllocator.free(outputBuffer);
        }
    }
}
