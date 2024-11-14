package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.schema.Column;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.Table;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.collections.Coll;

public final class StorageTableSchema {

    public static final class StorageSchemaColumn {

        private final SchemaDataType schemaDataType;

        private final int minBits;
        private final int maxBits;
        private final boolean isNullable;

        private StorageSchemaColumn(SchemaDataType schemaDataType) {

            this.schemaDataType = Objects.requireNonNull(schemaDataType);

            this.minBits = schemaDataType.getMinNumBits();
            this.maxBits = schemaDataType.getMaxNumBits();
            this.isNullable = schemaDataType.isNullable();
        }

        public SchemaDataType getSchemaDataType() {
            return schemaDataType;
        }

        public int getMinBits() {
            return minBits;
        }

        public int getMaxBits() {
            return maxBits;
        }

        public boolean isNullable() {
            return isNullable;
        }
    }

    private final int tableId;
    private final DatabaseSchemaVersion databaseSchemaVersion;

    private final StorageSchemaColumn[] columns;
    private final int totalMaxBits;

    StorageTableSchema(Table table, DatabaseSchemaVersion databaseSchemaVersion) {

        Objects.requireNonNull(table);
        Objects.requireNonNull(databaseSchemaVersion);

        this.tableId = table.getId();
        this.databaseSchemaVersion = databaseSchemaVersion;

        this.columns = Coll.toArray(table, table.getNumColumns(), StorageSchemaColumn[]::new, (t, i) -> toStorageSchemaColumn(t.getColumn(i)));

        this.totalMaxBits = Array.sum(columns, c -> c.maxBits);
    }

    DatabaseSchemaVersion getDatabaseSchemaVersion() {
        return databaseSchemaVersion;
    }

    public int getTableId() {
        return tableId;
    }

    public int getNumColumns() {

        return columns.length;
    }

    public StorageSchemaColumn getColumn(int index) {

        return columns[index];
    }

    public int getTotalMaxBits() {
        return totalMaxBits;
    }

    public boolean isSameTableAndShemaVersion(StorageTableSchema other) {

        Objects.requireNonNull(other);

        return tableId == other.tableId && databaseSchemaVersion.equals(other.databaseSchemaVersion);
    }

    private static StorageSchemaColumn toStorageSchemaColumn(Column tableColumn) {

        return new StorageSchemaColumn(tableColumn.getSchemaType());
    }
}
