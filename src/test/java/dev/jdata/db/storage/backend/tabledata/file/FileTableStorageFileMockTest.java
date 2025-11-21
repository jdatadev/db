package dev.jdata.db.storage.backend.tabledata.file;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.InOrder;
import org.mockito.Mockito;

import dev.jdata.db.storage.backend.tabledata.StorageTableSchema;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema.StorageTableFileSchemaGetters;
import dev.jdata.db.utils.adt.maps.IHeapMutableOrderedIntCountMap;
import dev.jdata.db.utils.adt.maps.IMutableOrderedIntCountMap;
import dev.jdata.db.utils.file.access.IFileSystemAccess.OpenMode;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RandomFileAccess;
import dev.jdata.db.utils.file.access.RelativeFilePath;

public final class FileTableStorageFileMockTest extends BaseFileTableStorageFileTest<FileTableStorageFileMockTest.MockTestData, RuntimeException> {

    static final class MockTestData extends BaseFileTableStorageFileTest.TestData {

        private final IRelativeFileSystemAccess fileSystemAccess;
        private final RandomFileAccess randomFileAccess;

        MockTestData(int sequenceNo, long startRowId, long initialTransactionId) {
            super(sequenceNo, startRowId, initialTransactionId);

            this.fileSystemAccess = Mockito.mock(IRelativeFileSystemAccess.class);
            this.randomFileAccess = Mockito.mock(RandomFileAccess.class);
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        RelativeFilePath getFilePath(String fileName) {

            return makeTestFilePath(fileSystemAccess, fileName);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testAppendNewFile() throws IOException {

        final FileInitializer<MockTestData> fileInitializer = (d, s) -> {

            final TestSchema testSchema = d.testSchema;
            final IRelativeFileSystemAccess fileSystemAccess = d.fileSystemAccess;
            final RandomFileAccess randomFileAccess = d.randomFileAccess;

            final RelativeFilePath filePath = d.getFilePath();

            Mockito.when(fileSystemAccess.openRandomFileAccess(eq(filePath), eq(OpenMode.READ_WRITE_CREATE_FAIL_IF_EXISTS))).thenReturn(randomFileAccess);

            final FileTableStorageFile fileTableStorageFile = FileTableStorageFile.addNewFile(fileSystemAccess, filePath, d.sequenceNo, testSchema.storageTableFileSchema,
                    d.initialTransactionId);

            Mockito.verify(fileSystemAccess).openRandomFileAccess(eq(filePath), eq(OpenMode.READ_WRITE_CREATE_FAIL_IF_EXISTS));

            verifyFileTableStorageFile(fileTableStorageFile, d);

            assertThat(fileTableStorageFile.getStorageTableFileSchema()).isSameAs(testSchema.storageTableFileSchema);

            verifyWriteSchema(randomFileAccess, testSchema.storageTableSchema, testSchema.getStorageFileSchema(s));

            return fileTableStorageFile;
        };

        checkAppend(fileInitializer);
    }

    @Test
    @Category(UnitTest.class)
    public void testAppendExistingFile() throws IOException {

        final FileInitializer<MockTestData> fileInitializer = (d, s) -> {

            final TestSchema testSchema = d.testSchema;
            final IRelativeFileSystemAccess fileSystemAccess = d.fileSystemAccess;
            final RandomFileAccess randomFileAccess = d.randomFileAccess;

            final RelativeFilePath filePath = d.getFilePath();

            Mockito.when(fileSystemAccess.openRandomFileAccess(eq(filePath), eq(OpenMode.READ_WRITE_EXISTING))).thenReturn(randomFileAccess);

            final TestDBSchemas dbSchemas = d.dbSchemas;

            final StorageTableFileSchemaGetters storageTableFileSchemaGetters = dbSchemas.getStorageTableFileSchemaGetters();

            final StorageFileSchema storageFileSchema = testSchema.getStorageFileSchema(s);

            stubReadSchema(randomFileAccess, testSchema.storageTableSchema, storageFileSchema);

            final FileTableStorageFile fileTableStorageFile = FileTableStorageFile.openExistingFile(fileSystemAccess, filePath, storageTableFileSchemaGetters);

            Mockito.verify(fileSystemAccess).openRandomFileAccess(eq(filePath), eq(OpenMode.READ_WRITE_EXISTING));

            verifyReadSchema(randomFileAccess, testSchema.storageTableSchema, storageFileSchema);

            verifyFileTableStorageFile(fileTableStorageFile, d);

            assertThat(fileTableStorageFile.getStorageTableFileSchema()).isEqualTo(testSchema.storageTableFileSchema);

            return fileTableStorageFile;
        };

        checkAppend(fileInitializer);
    }

    @Test
    @Category(UnitTest.class)
    public void testAppendExistingFileFromStorageTableFileSchema() throws IOException {

        final FileInitializer<MockTestData> fileInitializer = (d, s) -> {

            final TestSchema testSchema = d.testSchema;
            final IRelativeFileSystemAccess fileSystemAccess = d.fileSystemAccess;
            final RandomFileAccess randomFileAccess = d.randomFileAccess;

            final RelativeFilePath filePath = d.getFilePath();

            Mockito.when(fileSystemAccess.openRandomFileAccess(eq(filePath), eq(OpenMode.READ_WRITE_CREATE_FAIL_IF_EXISTS))).thenReturn(randomFileAccess);

            final FileTableStorageFile fileTableStorageFile = FileTableStorageFile.openExistingFileFromStorageTableFileSchema(fileSystemAccess, filePath, d.sequenceNo,
                    testSchema.storageTableFileSchema, d.startRowId);

//            verifyReadSchema(randomFileAccess, testSchema.storageTableSchema, testSchema.getStorageFileSchema());

            verifyFileTableStorageFile(fileTableStorageFile, d);

            assertThat(fileTableStorageFile.getStorageTableFileSchema()).isSameAs(testSchema.storageTableFileSchema);

            return fileTableStorageFile;
        };

        checkAppend(fileInitializer);
    }

    @Override
    MockTestData createTestData(int sequenceNo, long startRowId, long initialTransactionId) {

        return new MockTestData(sequenceNo, startRowId, initialTransactionId);
    }

    @Override
    void checkAppendRows(MockTestData testData, long startRowId, FileInitializer<MockTestData> fileInitializer) throws IOException {

        final IRelativeFileSystemAccess fileSystemAccess = testData.fileSystemAccess;
        final RandomFileAccess randomFileAccess = testData.randomFileAccess;

        final CheckAppendResult checkAppendResult = checkAppend(testData, startRowId, fileInitializer);

        Mockito.verifyNoMoreInteractions(fileSystemAccess, randomFileAccess);
        Mockito.reset(fileSystemAccess, randomFileAccess);

        Mockito.verify(randomFileAccess, times(checkAppendResult.numRows)).write(eq(checkAppendResult.rowBuffer), eq(0), eq(checkAppendResult.numRowBytes));

        Mockito.verifyNoMoreInteractions(randomFileAccess);
    }

    @Test
    @Category(UnitTest.class)
    public void testExpand() throws IOException {

        throw new UnsupportedOperationException();
    }

    private static void stubReadSchema(RandomFileAccess randomFileAccess, StorageTableSchema storageTableSchema, StorageFileSchema storageFileSchema) throws IOException {

        stubReadSchema(randomFileAccess, storageTableSchema, storageFileSchema.numRowIdBits, storageFileSchema.numTransactionIdBits, storageFileSchema.numColumBits,
                storageFileSchema.startRowId);
    }

    private static void stubReadSchema(RandomFileAccess randomFileAccess, StorageTableSchema storageTableSchema, int numRowIdBits, int numTransactionIdBits, int[] numColumnBits,
            long startRowId) throws IOException {

        Mockito.when(randomFileAccess.readInt()).thenReturn(storageTableSchema.getTableId());
        Mockito.when(randomFileAccess.readInt()).thenReturn(storageTableSchema.getDatabaseSchemaVersion().getVersionNumber());
        Mockito.when(randomFileAccess.readByte()).thenReturn((byte)numRowIdBits);
        Mockito.when(randomFileAccess.readByte()).thenReturn((byte)numTransactionIdBits);

        final int numColumns = numColumnBits.length;

        Mockito.when(randomFileAccess.readInt()).thenReturn(numColumns);

        for (int i = 0; i < numColumns; ++ i) {

            Mockito.when(randomFileAccess.readUnsignedByte()).thenReturn(numColumnBits[i]);
        }

        Mockito.when(randomFileAccess.readLong()).thenReturn(startRowId);
    }

    private static void verifyReadSchema(RandomFileAccess randomFileAccess, StorageTableSchema storageTableSchema, StorageFileSchema storageFileSchema) throws IOException {

        verifyReadSchema(randomFileAccess, storageTableSchema, storageFileSchema.numRowIdBits, storageFileSchema.numTransactionIdBits, storageFileSchema.numColumBits,
                storageFileSchema.startRowId);
    }

    private static void verifyReadSchema(RandomFileAccess randomFileAccess, StorageTableSchema storageTableSchema, int numRowIdBits, int numTransactionIdBits,
            int[] numColumnBits, long startRowId) throws IOException {

        final InOrder inOrder = Mockito.inOrder(randomFileAccess);

        inOrder.verify(randomFileAccess).readInt();
        inOrder.verify(randomFileAccess).readInt();
        inOrder.verify(randomFileAccess).readByte();
        inOrder.verify(randomFileAccess).readByte();

        final int numColumns = numColumnBits.length;

        inOrder.verify(randomFileAccess).writeInt(eq(numColumns));

        for (int i = 0; i < numColumns; ++ i) {

            inOrder.verify(randomFileAccess).readUnsignedByte();
        }

        inOrder.verify(randomFileAccess).readLong();

        inOrder.verifyNoMoreInteractions();
    }

    private static int verifyWriteSchema(RandomFileAccess randomFileAccess, StorageTableSchema storageTableSchema, StorageFileSchema storageFileSchema) throws IOException {

        return verifyWriteSchema(randomFileAccess, storageTableSchema, storageFileSchema.numRowIdBits, storageFileSchema.numTransactionIdBits, storageFileSchema.numColumBits,
                storageFileSchema.startRowId);
    }

    private static int verifyWriteSchema(RandomFileAccess randomFileAccess, StorageTableSchema storageTableSchema, int numRowIdBits, int numTransactionIdBits,
            int[] numColumnBits, long startRowId) throws IOException {

        final InOrder inOrder = Mockito.inOrder(randomFileAccess);

        inOrder.verify(randomFileAccess).writeInt(eq(storageTableSchema.getTableId()));
        inOrder.verify(randomFileAccess).writeInt(eq(storageTableSchema.getDatabaseSchemaVersion().getVersionNumber()));
        inOrder.verify(randomFileAccess).writeByte(numRowIdBits);
        inOrder.verify(randomFileAccess).writeByte(numTransactionIdBits);

        final int numColumns = numColumnBits.length;

        inOrder.verify(randomFileAccess).writeInt(eq(numColumns));

        final IMutableOrderedIntCountMap countMap = IHeapMutableOrderedIntCountMap.create(numColumns);

        countMap.add(numColumnBits);

        countMap.forEach((v, c) -> inOrder.verify(randomFileAccess, times(c)).writeByte(eq(v)));

        inOrder.verify(randomFileAccess).writeLong(eq(startRowId));

        inOrder.verifyNoMoreInteractions();

        return 4 + 4 + 1 + 1 + 4 + (1 * numColumns) + 8;
    }
}
