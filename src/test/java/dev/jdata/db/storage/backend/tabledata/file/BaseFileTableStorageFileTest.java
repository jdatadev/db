package dev.jdata.db.storage.backend.tabledata.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.common.storagebits.ParameterizedNumStorageBitsGetter;
import dev.jdata.db.common.storagebits.StorageMode;
import dev.jdata.db.engine.database.StringStorer;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchema;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.tabledata.file.BaseFileTableStorageFileTest.TestData;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema.NumColumnsBitsGetter;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema.StorageTableFileSchemaGetters;
import dev.jdata.db.test.unit.BaseDBTest;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.RelativeFilePath;

abstract class BaseFileTableStorageFileTest<T extends TestData, E extends Exception> extends BaseDBTest {

    static final class TestDBSchemas {

        final VersionedDatabaseSchemas versionedDatabaseSchemas;
        final DatabaseSchemaVersion databaseSchemaVersion;
        final StorageTableSchemas storageTableSchemas;

        TestDBSchemas(VersionedDatabaseSchemas versionedDatabaseSchemas, DatabaseSchemaVersion databaseSchemaVersion, StorageTableSchemas storageTableSchemas) {

            this.versionedDatabaseSchemas = Objects.requireNonNull(versionedDatabaseSchemas);
            this.databaseSchemaVersion = Objects.requireNonNull(databaseSchemaVersion);
            this.storageTableSchemas = Objects.requireNonNull(storageTableSchemas);
        }

        StorageTableSchema getStorageTableSchema(int tableId) {

            Checks.isTableId(tableId);

            return storageTableSchemas.getStorageTableSchema(tableId, databaseSchemaVersion);
        }

        StorageTableFileSchemaGetters getStorageTableFileSchemaGetters() {

            return new StorageTableFileSchemaGetters(versionedDatabaseSchemas, storageTableSchemas);
        }
    }

    static abstract class TestData implements Closeable {

        final TestDBSchemas dbSchemas;
        final int sequenceNo;
        final long startRowId;
        final long initialTransactionId;
        final TestSchema testSchema;

        private final Table table;
        private final String fileName;

        abstract RelativeFilePath getFilePath(String fileName);

        TestData(int sequenceNo, long startRowId, long initialTransactionId) {

            Checks.isSequenceNo(sequenceNo);
            Checks.isRowId(startRowId);
            Checks.isTransactionId(initialTransactionId);

            final DatabaseSchemaVersion databaseSchemaVersion = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

            final int tableId = Table.INITIAL_TABLE_ID;

            final StringStorer stringStorer = createStringStorer();

            final Table table = createTestTable(tableId, stringStorer);

            final HeapIndexListAllocator<Table> tableIndexListAllocator = new HeapIndexListAllocator<>(Table[]::new);
            final HeapIndexListAllocator<IDatabaseSchema> databaseSchemaIndexListAllocator = new HeapIndexListAllocator<>(IDatabaseSchema[]::new);
            final ILongToObjectMaxDistanceMapAllocator<Table> longToObjectMapAllocator = null;

            final TestAllocators testAllocators = new TestAllocators(tableIndexListAllocator, longToObjectMapAllocator, databaseSchemaIndexListAllocator);

            this.dbSchemas = prepareStorageTableSchemas(databaseSchemaVersion, testAllocators, table);

            this.sequenceNo = sequenceNo;
            this.startRowId = startRowId;
            this.initialTransactionId = initialTransactionId;

            this.fileName = FileTableStorageFile.constructTableFileName(sequenceNo);

            this.table = table;

            this.testSchema = makeTestSchema(dbSchemas, table);
        }

        final RelativeFilePath getFilePath() {

            return getFilePath(fileName);
        }
    }

    static final class StorageFileSchema {

        final int numRowIdBits;
        final int numTransactionIdBits;
        final int[] numColumBits;
        final long startRowId;

        StorageFileSchema(int numRowIdBits, int numTransactionIdBits, int[] numColumBits, long startRowId) {

            this.numRowIdBits = Checks.isNumBits(numRowIdBits);
            this.numTransactionIdBits = Checks.isNumBits(numTransactionIdBits);
            this.numColumBits = Checks.isNotEmpty(numColumBits);
            this.startRowId = Checks.isRowId(startRowId);
        }
    }

    static final class TestSchema {

        final int maxRows;
        final int maxTransactions;

        final int numRowIdBits;
        final int numDeleteMarkerBits;
        final int numTransactionIdBits;

        final int numColumns;
        final int numNullableColumns;
        final int[] numColumnBits;
        final int numNullValueBitMapBits;
        final int totalNumColumnBits;
        final int totalRowNumBits;

        final StorageTableSchema storageTableSchema;
        final StorageTableFileSchema storageTableFileSchema;

        TestSchema(TestDBSchemas dbSchemas, Table table, int maxRows, int maxTransactions) {

            Objects.requireNonNull(dbSchemas);
            Objects.requireNonNull(table);
            Checks.isAboveZero(maxRows);
            Checks.isAboveZero(maxTransactions);

            this.maxRows = maxRows;
            this.maxTransactions = maxTransactions;

            this.numRowIdBits = BitsUtil.getNumStorageBits(maxRows, false);
            this.numDeleteMarkerBits = 1;
            this.numTransactionIdBits = BitsUtil.getNumStorageBits(maxTransactions, false);

            this.numColumns = table.getNumColumns();
            this.numNullableColumns = table.getNumNullableColumns();

            Assertions.areEqual(numNullableColumns, numColumns);

            this.numColumnBits = new int[numColumns];

            for (int i = 0; i < numColumns; ++ i) {

                numColumnBits[i] = 7;
            }

            this.numNullValueBitMapBits = table.getNumNullableColumns();
            this.totalNumColumnBits = Array.sum(numColumnBits);
            this.totalRowNumBits = numRowIdBits + numDeleteMarkerBits + numTransactionIdBits + numNullValueBitMapBits + totalNumColumnBits;

            final NumColumnsBitsGetter<RuntimeException> numColumnsBitsGetter = i -> numColumnBits[i];

            this.storageTableSchema = dbSchemas.getStorageTableSchema(table.getId());

            this.storageTableFileSchema = new StorageTableFileSchema(storageTableSchema, numRowIdBits, numDeleteMarkerBits, numTransactionIdBits, numNullValueBitMapBits,
                    numColumnsBitsGetter);
        }

        StorageFileSchema getStorageFileSchema(long startRowId) {

            return new StorageFileSchema(numRowIdBits, numTransactionIdBits, numColumnBits, startRowId);
        }
    }

    @FunctionalInterface
    interface FileInitializer<T extends TestData> {

        FileTableStorageFile initialize(T testData, long startRowId) throws IOException;
    }

    static final class CheckAppendResult {

        final byte columnValue;
        final int numRows;
        final byte[] rowBuffer;
        final int numRowBytes;

        CheckAppendResult(byte columnValue, int numRows, byte[] rowBuffer, int numRowBytes) {

            this.columnValue = columnValue;
            this.numRows = Checks.isAboveZero(numRows);
            this.rowBuffer = Objects.requireNonNull(rowBuffer);
            this.numRowBytes = Checks.isAboveZero(numRowBytes);
        }
    }

    private static final class TestAllocators {

        private final HeapIndexListAllocator<Table> tableIndexListAllocator;
        private final ILongToObjectMaxDistanceMapAllocator<Table> longToObjectMapAllocator;
        private final HeapIndexListAllocator<IDatabaseSchema> databaseSchemaIndexListAllocator;

        TestAllocators(HeapIndexListAllocator<Table> tableIndexListAllocator, ILongToObjectMaxDistanceMapAllocator<Table> longToObjectMapAllocator,
                HeapIndexListAllocator<IDatabaseSchema> databaseSchemaIndexListAllocator) {

            this.tableIndexListAllocator = Objects.requireNonNull(tableIndexListAllocator);
            this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
            this.databaseSchemaIndexListAllocator = Objects.requireNonNull(databaseSchemaIndexListAllocator);
        }
    }

    abstract T createTestData(int sequenceNo, long startRowId, long initialTransactionId) throws E;
    abstract void checkAppendRows(T testData, long startRowId, FileInitializer<T> fileInitializer) throws IOException;

    final void checkAppend(FileInitializer<T> fileInitializer) throws E, IOException {

        final int sequenceNo = FileTableStorageFiles.INITIAL_SEQUENCE_NO;

        final long startRowId = 0L;
        final long initialTransactionId = TEST_TRANSACTION_ID;

        try (T testData = createTestData(sequenceNo, startRowId, initialTransactionId)) {

            checkAppendRows(testData, startRowId, fileInitializer);
        }
    }

    static <T extends TestData> CheckAppendResult checkAppend(T testData, long startRowId, FileInitializer<T> fileInitializer) throws IOException {

        Objects.requireNonNull(testData);
        Checks.isRowId(startRowId);
        Objects.requireNonNull(fileInitializer);

        final FileTableStorageFile fileTableStorageFile = fileInitializer.initialize(testData, startRowId);

        assertThat(fileTableStorageFile.getFileName()).isEqualTo(testData.getFilePath().getFileName());

        final TestSchema testSchema = testData.testSchema;

        final int numRowBytes = BitBufferUtil.numBytes(testSchema.totalRowNumBits);

        final byte[] rowBuffer = new byte[numRowBytes];

        final byte columnValue = (byte)0b10101010;

//        byte[] expectedOutput = null;

        final int numRows = forEachRow(testData, (rowId, deleteMarkerValue, transactionId, numRowsParameter) -> {

            Arrays.fill(rowBuffer, (byte)0);

            writeRowToBuffer(rowBuffer, testSchema, rowId, deleteMarkerValue, transactionId, columnValue);

            fileTableStorageFile.append(1, rowBuffer, numRowBytes, false);
/*
            if (expectedOutput == null) {

                expectedOutput = Arrays.copyOf(rowBuffer, rowBuffer.length);
            }
*/
            assertThat(fileTableStorageFile.getNumRows()).isEqualTo(numRowsParameter);
            assertThat(fileTableStorageFile.getLastRowId()).isEqualTo(testData.startRowId + numRowsParameter - 1);
        });

        long bitOffset = 0L;

        for (long i = 0L; i < numRows; ++ i) {

            final long rowId = testData.startRowId + i;

            assertThat(fileTableStorageFile.getRowBitOffset(rowId)).isEqualTo(bitOffset);

            bitOffset += testSchema.totalRowNumBits;
        }

        return new CheckAppendResult(columnValue, numRows, rowBuffer, numRowBytes);
    }

    @FunctionalInterface
    interface ForRows<E extends Exception> {

        void each(long rowId, boolean deleteMarkerValue, long transactionId, int numRows) throws E;
    }

    static <T extends TestData, E extends Exception> int forEachRow(T testData, ForRows<E> forRows) throws E {

        Objects.requireNonNull(testData);

        final TestSchema testSchema = testData.testSchema;

        final boolean[] deleteMarkerValues = new boolean[] { false, true };

        final long maxTransactionId = testData.initialTransactionId + testSchema.maxTransactions;

        int numRows = 0;

        for (long rowId = testData.startRowId; rowId < testSchema.maxRows; ++ rowId) {

            for (boolean deleteMarkerValue : deleteMarkerValues) {

                for (long transactionId = 0; transactionId < maxTransactionId; ++ transactionId) {

                    ++ numRows;

                    forRows.each(rowId, deleteMarkerValue, maxTransactionId, numRows);
                }
            }
        }

        return numRows;
    }

    static void verifyFileTableStorageFile(FileTableStorageFile fileTableStorageFile, TestData testData) {

        assertThat(fileTableStorageFile.getSequenceNo()).isEqualTo(testData.sequenceNo);
        assertThat(fileTableStorageFile.getStartRowId()).isEqualTo(testData.startRowId);
    }

    static int verifyRowBuffer(byte[] rowBuffer, int startBitOffset, TestSchema testSchema, long rowId, boolean deleteMarkerValue, long transactionId, byte columnValue) {

        int bitOffset = startBitOffset;

        assertThat(BitBufferUtil.getLongValue(rowBuffer, false, bitOffset, testSchema.numRowIdBits)).isEqualTo(rowId);

        bitOffset += testSchema.numRowIdBits;

        assertThat(BitBufferUtil.getIntValue(rowBuffer, false, bitOffset, testSchema.numDeleteMarkerBits) != 0).isEqualTo(deleteMarkerValue);

        bitOffset += testSchema.numDeleteMarkerBits;

        assertThat(BitBufferUtil.getLongValue(rowBuffer, false, bitOffset, testSchema.numTransactionIdBits)).isEqualTo(transactionId);

        bitOffset += testSchema.numTransactionIdBits;

        for (int i = 0; i < testSchema.numColumns; ++ i) {

            final int numBits = testSchema.numColumnBits[i];

            Assertions.isLessThan(numBits, 8);

            final int value = columnValue & BitsUtil.mask(numBits);

            assertThat(BitBufferUtil.getIntValue(rowBuffer, false, bitOffset, numBits)).isEqualTo(value);

            bitOffset += numBits;
        }

        return bitOffset;
    }

    private static void writeRowToBuffer(byte[] rowBuffer, TestSchema testSchema, long rowId, boolean deleteMarkerValue, long transactionId, byte columnValue) {

        long bitOffset = 0L;

        BitBufferUtil.setLongValue(rowBuffer, rowId, false, bitOffset, testSchema.numRowIdBits);

        bitOffset += testSchema.numRowIdBits;

        BitBufferUtil.setBits(rowBuffer, deleteMarkerValue, bitOffset, testSchema.numDeleteMarkerBits);

        bitOffset += testSchema.numDeleteMarkerBits;

        BitBufferUtil.setLongValue(rowBuffer, transactionId, false, bitOffset, testSchema.numTransactionIdBits);

        bitOffset += testSchema.numTransactionIdBits;

        for (int i = 0; i < testSchema.numColumns; ++ i) {

            final int numBits = testSchema.numColumnBits[i];

            Assertions.isLessThan(numBits, 8);

            final int value = columnValue & BitsUtil.mask(numBits);

            BitBufferUtil.setIntValue(rowBuffer, value, false, bitOffset, numBits);
        }
    }

    private static TestSchema makeTestSchema(TestDBSchemas dbSchemas, Table table) {

        Objects.requireNonNull(dbSchemas);
        Objects.requireNonNull(table);

        final int maxRows = 10;
        final int maxTransactions = 3;

        return new TestSchema(dbSchemas, table, maxRows, maxTransactions);
    }

    private static StorageTableSchema prepareStorageTableSchema(Table table, TestAllocators testAllocators) {

        Objects.requireNonNull(table);
        Objects.requireNonNull(testAllocators);

        final DatabaseSchemaVersion databaseSchemaVersion = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

        return prepareStorageTableSchema(table, testAllocators, databaseSchemaVersion);
    }

    private static StorageTableSchema prepareStorageTableSchema(Table table, TestAllocators testAllocators, DatabaseSchemaVersion databaseSchemaVersion) {

        Objects.requireNonNull(table);
        Objects.requireNonNull(testAllocators);

        return prepareStorageTableSchemas(databaseSchemaVersion, testAllocators, table).getStorageTableSchema(table.getId());
    }

    private static TestDBSchemas prepareStorageTableSchemas(DatabaseSchemaVersion databaseSchemaVersion, TestAllocators testAllocators, Table ... tables) {

        Objects.requireNonNull(databaseSchemaVersion);
        Objects.requireNonNull(testAllocators);
        Checks.isNotEmpty(tables);
        Checks.areElements(tables, Objects::nonNull);

        final HeapIndexList<Table> tableList = IndexList.of(tables);

        final HeapSchemaMap<Table> tablesSchemaMap = HeapSchemaMap.of(tableList, Table[]::new, testAllocators.longToObjectMapAllocator);

        final HeapAllCompleteSchemaMaps schemaMaps = HeapAllCompleteSchemaMaps.empty();

        final DatabaseId databaseId = new DatabaseId(DBConstants.INITIAL_DESCRIPTORABLE, TEST_DATABASE_NAME);

        final CompleteDatabaseSchema databaseSchema = CompleteDatabaseSchema.of(databaseId, databaseSchemaVersion, schemaMaps);

        final VersionedDatabaseSchemas versionedDatabaseSchemas = VersionedDatabaseSchemas.of(databaseId, IndexList.of(databaseSchema),
                testAllocators.databaseSchemaIndexListAllocator);

        final StorageMode storageMode = StorageMode.INT_REFERENCE;

        final int minBits = 1;

        final NumStorageBitsParameters numStorageBitsParameters = new NumStorageBitsParameters(minBits, minBits, minBits, minBits, storageMode, null, storageMode, storageMode);
        final INumStorageBitsGetter numStorageBitsGetter = new ParameterizedNumStorageBitsGetter(numStorageBitsParameters);

        final StorageTableSchemas storageTableSchemas = StorageTableSchemas.of(versionedDatabaseSchemas, numStorageBitsGetter);

        return new TestDBSchemas(versionedDatabaseSchemas, databaseSchemaVersion, storageTableSchemas);
    }
}
