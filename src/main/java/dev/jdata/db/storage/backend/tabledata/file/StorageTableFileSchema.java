package dev.jdata.db.storage.backend.tabledata.file;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchema;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.utils.checks.Checks;

public final class StorageTableFileSchema {

    public static final int NO_NULL_VALUE_BITMAP_INDEX = -1;

    private static final int MAX_COLUMN_ROW_NUM_BITS = Byte.MAX_VALUE;

    public static final class StorageTableFileColumn {

        private final int bitOffset;
        private final int numBits;
        private final int nullValueBitmapIndex;

        StorageTableFileColumn(int bitOffset, int numBits, int nullValueBitmapIndex) {

            Checks.isOffset(bitOffset);
            Checks.isNumBits(numBits);

            if (numBits > MAX_COLUMN_ROW_NUM_BITS) {

                throw new IllegalArgumentException();
            }

            Checks.isIndex(nullValueBitmapIndex);

            this.bitOffset = bitOffset;
            this.numBits = numBits;
            this.nullValueBitmapIndex = nullValueBitmapIndex;
        }

        public int getNumBits() {
            return numBits;
        }

        public int getNullValueBitmapIndex() {
            return nullValueBitmapIndex;
        }

        @Override
        public boolean equals(Object object) {

            final boolean result;

            if (this == object) {

                result = true;
            }
            else if (object == null) {

                result = false;
            }
            else if (getClass() != object.getClass()) {

                result = false;
            }
            else {
                final StorageTableFileColumn other = (StorageTableFileColumn)object;

                result = bitOffset == other.bitOffset && nullValueBitmapIndex == other.nullValueBitmapIndex && numBits == other.numBits;
            }

            return result;
        }
    }

    @FunctionalInterface
    public interface NumColumnsBitsGetter<E extends Exception> {

        int getColumnNumBits(int columnIndex) throws E;
    }

    @FunctionalInterface
    public interface StorageTableSchemaGetter {

        StorageTableSchema getStorageTableSchema(int tableId, DatabaseSchemaVersion databaseSchemaVersion);
    }

    @FunctionalInterface
    public interface NumNullValueBitMapBitsGetter {

        int getNumNullValueBitMapBits(int tableId, DatabaseSchemaVersion databaseSchemaVersion);
    }

    public static final class StorageTableFileSchemaGetters {

        public final StorageTableSchemaGetter storageTableSchemaGetter;
        public final NumNullValueBitMapBitsGetter numNullValueBitMapBitsGetter;

        public StorageTableFileSchemaGetters(VersionedDatabaseSchemas versionedDatabaseSchemas, StorageTableSchemas storageTableSchemas) {
            this(storageTableSchemas::getStorageTableSchema, (t, v) -> getNumNullValueBitmapBits(t, v, versionedDatabaseSchemas));

            Objects.requireNonNull(versionedDatabaseSchemas);
            Objects.requireNonNull(storageTableSchemas);
        }

        private StorageTableFileSchemaGetters(StorageTableSchemaGetter storageTableSchemaGetter, NumNullValueBitMapBitsGetter numNullValueBitMapBitsGetter) {

            this.storageTableSchemaGetter = Objects.requireNonNull(storageTableSchemaGetter);
            this.numNullValueBitMapBitsGetter = Objects.requireNonNull(numNullValueBitMapBitsGetter);
        }


        public StorageTableSchema getStorageTableSchema(int tableId, DatabaseSchemaVersion databaseSchemaVersion) {

            return storageTableSchemaGetter.getStorageTableSchema(tableId, databaseSchemaVersion);
        }

        public int getNumNullValueBitMapBits(int tableId, DatabaseSchemaVersion databaseSchemaVersion) {

            return numNullValueBitMapBitsGetter.getNumNullValueBitMapBits(tableId, databaseSchemaVersion);
        }

        private static int getNumNullValueBitmapBits(int tableId, DatabaseSchemaVersion databaseSchemaVersion, VersionedDatabaseSchemas versionedDatabaseSchemas) {

            final IDatabaseSchema databaseSchema = versionedDatabaseSchemas.getSchema(databaseSchemaVersion);

            final Table table = databaseSchema.getTable(tableId);

            return table.getNumNullableColumns();
        }
    }

    private final StorageTableSchema storageTableSchema;

    private final int numRowIdBits;
    private final int numDeleteMarkerBits;
    private final int numTransactionIdBits;
    private final int numNullValueBitMapBits;
    private final StorageTableFileColumn[] columns;
    private final int numRowDataBits;
    private final int totalNumRowBits;
    private final int totalNumRowBytes;

    <E extends Exception> StorageTableFileSchema(StorageTableSchema storageTableSchema, int numRowIdBits, int numDeleteMarkerBits, int numTransactionIdBits,
            int numNullValueBitMapBits, NumColumnsBitsGetter<E> numBitsGetter) throws E {

        Objects.requireNonNull(storageTableSchema);
        Checks.isNumBits(numRowIdBits);
        Checks.isNumBits(numDeleteMarkerBits);
        Checks.isNumBits(numTransactionIdBits);
        Checks.isNumBits(numNullValueBitMapBits);
        Objects.requireNonNull(numBitsGetter);

        this.storageTableSchema = storageTableSchema;
        this.numRowIdBits = numRowIdBits;
        this.numDeleteMarkerBits = numDeleteMarkerBits;
        this.numTransactionIdBits = numTransactionIdBits;
        this.numNullValueBitMapBits = numNullValueBitMapBits;

        final int numColumns = storageTableSchema.getNumColumns();

        this.columns = new StorageTableFileColumn[numColumns];

        int columnBitOffset = 0;

        int numNullableColumns = 0;

        for (int i = 0; i < numColumns; ++ i) {

            final int columnNumBits = numBitsGetter.getColumnNumBits(i);

            final boolean isNullable = storageTableSchema.getColumn(i).isNullable();

            final int nullValueBitmapIndex = isNullable ? numNullableColumns ++ : NO_NULL_VALUE_BITMAP_INDEX;

            final StorageTableFileColumn storageColumn = new StorageTableFileColumn(columnBitOffset, columnNumBits, nullValueBitmapIndex);

            columnBitOffset += columnNumBits;

            columns[i] = storageColumn;
        }

        this.numRowDataBits = columnBitOffset;
        this.totalNumRowBits = numRowIdBits + numDeleteMarkerBits + numTransactionIdBits + numNullValueBitMapBits + columnBitOffset;
        this.totalNumRowBytes = ((totalNumRowBits - 1) / 8) + 1;
    }

    private <E extends Exception> StorageTableFileSchema(StorageTableFileSchema toIncrement, int numRowIdBits, int numDeleteMarkerBits, int numTransactionIdBits,
            int numNullValueBitMapBits, int[] columnsToIncrement, NumColumnsBitsGetter<E> numBitsGetter) throws E {
        this(toIncrement.storageTableSchema, numRowIdBits, numDeleteMarkerBits, numTransactionIdBits, numNullValueBitMapBits, i -> {

            @Deprecated // TODO increment more than 1
            boolean incrementColumn = false;

            for (int columnToIncrement : columnsToIncrement) {

                if (columnToIncrement == i) {

                    if (incrementColumn) {

                        throw new IllegalArgumentException();
                    }

                    incrementColumn = true;
                }
            }

            int columnNumBits = numBitsGetter.getColumnNumBits(i);

            if (incrementColumn) {

                ++ columnNumBits;
            }

            return columnNumBits;
        });
    }

    public StorageTableFileSchema adjustNumBits(int numRowIdBits, int numDeleteMarkerBits, int numTransactionIdBits, int numNullValueBitMapBits) {

        return new StorageTableFileSchema(this, numRowIdBits, numDeleteMarkerBits, numTransactionIdBits, numNullValueBitMapBits, null, null);
    }

    public static StorageTableFileSchema read(DataInput dataInput, StorageTableFileSchemaGetters storageTableFileSchemaGetters)
            throws IOException {

        Objects.requireNonNull(dataInput);
        Objects.requireNonNull(storageTableFileSchemaGetters);

        final int tableId = dataInput.readInt();

        final int databaseSchemaVersionNumber = dataInput.readInt();

        final int numRowIdBits = dataInput.readUnsignedByte();
        final int numDeleteMarkerBits = dataInput.readUnsignedByte();
        final int numTransactionIdBits = dataInput.readUnsignedByte();

        final int numColumns = dataInput.readInt();

        final DatabaseSchemaVersion databaseSchemaVersion = DatabaseSchemaVersion.of(databaseSchemaVersionNumber);

        final int numNullValueBitMapBits = storageTableFileSchemaGetters.getNumNullValueBitMapBits(tableId, databaseSchemaVersion);

        final StorageTableSchema storageTableSchema = storageTableFileSchemaGetters.getStorageTableSchema(tableId, databaseSchemaVersion);

        if (numColumns != storageTableSchema.getNumColumns()) {

            throw new IllegalStateException();
        }

        return new StorageTableFileSchema(storageTableSchema, numRowIdBits, numDeleteMarkerBits, numTransactionIdBits, numNullValueBitMapBits, i-> dataInput.readUnsignedByte());
    }

    public int getNumHeaderBytes() {

        return 4 + 4 + 1 + 1 + 4 + (1 * columns.length);
    }

    private int getNumHeaderBits() {

        return getNumHeaderBytes() * 8;
    }

    public void write(DataOutput dataOutput) throws IOException {

        Objects.requireNonNull(dataOutput);

        dataOutput.writeInt(storageTableSchema.getTableId());
        dataOutput.writeInt(storageTableSchema.getDatabaseSchemaVersion().getVersionNumber());

        dataOutput.writeByte(numRowIdBits);
        dataOutput.writeByte(numTransactionIdBits);

        dataOutput.writeInt(columns.length);

        for (StorageTableFileColumn storageColumn : columns) {

            dataOutput.writeByte(storageColumn.numBits);
        }
    }

    public StorageTableSchema getStorageTableSchema() {
        return storageTableSchema;
    }

    public int getNumRowIdBits() {
        return numRowIdBits;
    }

    public int getNumDeleteMarkerBits() {
        return numDeleteMarkerBits;
    }

    public int getNumTransactionIdBits() {
        return numTransactionIdBits;
    }

    public int getNumNullValueBitMapBits() {
        return numNullValueBitMapBits;
    }

    public int getNumRowDataBits() {
        return numRowDataBits;
    }

    public int getNumColumns() {

        return columns.length;
    }

    public StorageTableFileColumn getStorageColumn(int index) {

        return columns[index];
    }

    public int getTotalNumRowBits() {
        return totalNumRowBits;
    }

    public int getTotalNumRowBytes() {
        return totalNumRowBytes;
    }

    public int getNullValueBitmapIndex(int columnIndex) {

        return columns[columnIndex].nullValueBitmapIndex;
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (object == null) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final StorageTableFileSchema other = (StorageTableFileSchema)object;

            result = numDeleteMarkerBits == other.numDeleteMarkerBits && numNullValueBitMapBits == other.numNullValueBitMapBits && numRowDataBits == other.numRowDataBits
                && numRowIdBits == other.numRowIdBits && numTransactionIdBits == other.numTransactionIdBits && totalNumRowBits == other.totalNumRowBits
                && totalNumRowBytes == other.totalNumRowBytes && Arrays.equals(columns, other.columns) && Objects.equals(storageTableSchema, other.storageTableSchema);
        }

        return result;
    }
}
