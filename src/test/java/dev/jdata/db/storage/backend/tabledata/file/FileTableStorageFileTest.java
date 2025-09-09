package dev.jdata.db.storage.backend.tabledata.file;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema.StorageTableFileSchemaGetters;
import dev.jdata.db.test.TestFileSystemAccess;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeFilePath;

public final class FileTableStorageFileTest extends BaseFileTableStorageFileTest<FileTableStorageFileTest.FileTestData, IOException> {

    static final class FileTestData extends BaseFileTableStorageFileTest.TestData {

        private final TestFileSystemAccess testFileSystemAccess;
        private final IRelativeFileSystemAccess fileSystemAccess;

        FileTestData(int sequenceNo, long startRowId, long initialTransactionId) throws IOException {
            super(sequenceNo, startRowId, initialTransactionId);

            this.testFileSystemAccess = TestFileSystemAccess.create();
            this.fileSystemAccess = testFileSystemAccess.createRelative();
        }

        @Override
        public void close() throws IOException {

            testFileSystemAccess.close();
        }

        @Override
        RelativeFilePath getFilePath(String fileName) {

            return fileSystemAccess.filePathOf(fileName);
        }

        private DataInputStream openDataInputStream() throws IOException {

            return openDataInputStream(getFilePath());
        }

        private DataInputStream openDataInputStream(RelativeFilePath filePath) throws IOException {

            Objects.requireNonNull(filePath);

            return fileSystemAccess.openDataInputStream(filePath);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testAppendNewFile() throws IOException {

        final FileInitializer<FileTestData> fileInitializer = (d, s) -> {

            final TestSchema testSchema = d.testSchema;
            final IRelativeFileSystemAccess fileSystemAccess = d.fileSystemAccess;

            final RelativeFilePath filePath = d.getFilePath();

            final FileTableStorageFile fileTableStorageFile = FileTableStorageFile.addNewFile(fileSystemAccess, filePath, d.sequenceNo, testSchema.storageTableFileSchema,
                    d.initialTransactionId);

            verifyFileTableStorageFile(fileTableStorageFile, d);

            assertThat(fileTableStorageFile.getStorageTableFileSchema()).isSameAs(testSchema.storageTableFileSchema);

            return fileTableStorageFile;
        };

        checkAppend(fileInitializer);
    }

    @Test
    @Category(UnitTest.class)
    public void testAppendExistingFile() throws IOException {

        final FileInitializer<FileTestData> fileInitializer = (d, s) -> {

            final TestSchema testSchema = d.testSchema;
            final IRelativeFileSystemAccess fileSystemAccess = d.fileSystemAccess;

            final TestDBSchemas dbSchemas = d.dbSchemas;

            final RelativeFilePath filePath = d.getFilePath();

            final StorageTableFileSchemaGetters storageTableFileSchemaGetters = new StorageTableFileSchemaGetters(dbSchemas.versionedDatabaseSchemas,
                    dbSchemas.storageTableSchemas);

            final FileTableStorageFile fileTableStorageFile = FileTableStorageFile.openExistingFile(fileSystemAccess, filePath, storageTableFileSchemaGetters);

            verifyFileTableStorageFile(fileTableStorageFile, d);

            assertThat(fileTableStorageFile.getStorageTableFileSchema()).isEqualTo(testSchema.storageTableFileSchema);

            return fileTableStorageFile;
        };

        checkAppend(fileInitializer);
    }

    @Test
    @Category(UnitTest.class)
    public void testAppendExistingFileFromStorageTableFileSchema() throws IOException {

        final FileInitializer<FileTestData> fileInitializer = (d, s) -> {

            final TestSchema testSchema = d.testSchema;
            final IRelativeFileSystemAccess fileSystemAccess = d.fileSystemAccess;

            final RelativeFilePath filePath = d.getFilePath();

            final FileTableStorageFile fileTableStorageFile = FileTableStorageFile.openExistingFileFromStorageTableFileSchema(fileSystemAccess, filePath, d.sequenceNo,
                    testSchema.storageTableFileSchema, d.startRowId);

            verifyFileTableStorageFile(fileTableStorageFile, d);

            assertThat(fileTableStorageFile.getStorageTableFileSchema()).isSameAs(testSchema.storageTableFileSchema);

            return fileTableStorageFile;
        };

        checkAppend(fileInitializer);
    }

    @Override
    FileTestData createTestData(int sequenceNo, long startRowId, long initialTransactionId) throws IOException {

        return new FileTestData(sequenceNo, startRowId, initialTransactionId);
    }

    @Override
    void checkAppendRows(FileTestData testData, long startRowId, FileInitializer<FileTestData> fileInitializer) throws IOException {

        final TestSchema testSchema = testData.testSchema;

        final CheckAppendResult checkAppendResult = checkAppend(testData, startRowId, fileInitializer);

        final int allRowsNumBytes = BitBufferUtil.numBytes(testSchema.totalRowNumBits * checkAppendResult.numRows);

        final byte[] rowsBuffer = new byte[allRowsNumBytes];

        try (DataInputStream dataInputStream = testData.openDataInputStream()) {

            final StorageTableFileSchemaGetters storageTableFileSchemaGetters = testData.dbSchemas.getStorageTableFileSchemaGetters();

            final StorageTableFileSchema storageTableFileSchema = StorageTableFileSchema.read(dataInputStream, storageTableFileSchemaGetters);

            assertThat(storageTableFileSchema).isEqualTo(testSchema.storageTableFileSchema);

            assertThat(dataInputStream.readLong()).isEqualTo(startRowId);

            forEachRow(testData, ((rowId, deleteMarkerValue, transactionId, numRows) -> {

                dataInputStream.readFully(rowsBuffer);

                int bitOffset = 0;

                for (int i = 0; i < numRows; ++ i) {

                    final int afterRowBitOffset = verifyRowBuffer(rowsBuffer, bitOffset, testSchema, startRowId, deleteMarkerValue, transactionId,
                            checkAppendResult.columnValue);

                    bitOffset += testSchema.totalRowNumBits;

                    assertThat(bitOffset).isEqualTo(afterRowBitOffset);
                }
            }));
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testExpand() throws IOException {

        throw new UnsupportedOperationException();
    }
}
