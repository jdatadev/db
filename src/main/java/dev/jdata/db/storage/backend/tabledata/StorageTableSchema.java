package dev.jdata.db.storage.backend.tabledata;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.collections.Coll;

public final class StorageTableSchema {

    public static final class StorageSchemaColumn {

        private final SchemaDataType schemaDataType;

        private final int minBits;
        private final int maxBits;
        private final boolean isNullable;

        private StorageSchemaColumn(SchemaDataType schemaDataType, boolean isNullable, INumStorageBitsGetter numStorageBitsGetter) {

            Objects.requireNonNull(schemaDataType);
            Objects.requireNonNull(numStorageBitsGetter);

            this.schemaDataType = schemaDataType;

            this.minBits = numStorageBitsGetter.getMinNumBits(schemaDataType);
            this.maxBits = numStorageBitsGetter.getMaxNumBits(schemaDataType);
            this.isNullable = isNullable;
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
                final StorageSchemaColumn other = (StorageSchemaColumn)object;

                result = isNullable == other.isNullable && maxBits == other.maxBits && minBits == other.minBits && Objects.equals(schemaDataType, other.schemaDataType);
            }

            return result;
        }
    }

    private final int tableId;
    private final DatabaseSchemaVersion databaseSchemaVersion;

    private final StorageSchemaColumn[] columns;
    private final int totalMaxBits;

    StorageTableSchema(Table table, DatabaseSchemaVersion databaseSchemaVersion, INumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(table);
        Objects.requireNonNull(databaseSchemaVersion);
        Objects.requireNonNull(numStorageBitsGetter);

        this.tableId = table.getId();
        this.databaseSchemaVersion = databaseSchemaVersion;

        this.columns = Coll.toArray(table, table.getNumColumns(), StorageSchemaColumn[]::new, (t, i) -> toStorageSchemaColumn(t.getColumn(i), numStorageBitsGetter));

        this.totalMaxBits = Array.sum(columns, c -> c.maxBits);
    }

    public DatabaseSchemaVersion getDatabaseSchemaVersion() {
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

    public boolean isSameTableAndSchemaVersion(StorageTableSchema other) {

        Objects.requireNonNull(other);

        return tableId == other.tableId && databaseSchemaVersion.equals(other.databaseSchemaVersion);
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
            final StorageTableSchema other = (StorageTableSchema)object;

            result = databaseSchemaVersion.equals(other.databaseSchemaVersion) && tableId == other.tableId && totalMaxBits == other.totalMaxBits
                    && Arrays.equals(columns, other.columns);
        }

        return result;
    }

    private static StorageSchemaColumn toStorageSchemaColumn(Column tableColumn, INumStorageBitsGetter numStorageBitsGetter) {

        return new StorageSchemaColumn(tableColumn.getSchemaType(), tableColumn.isNullable(), numStorageBitsGetter);
    }
}
