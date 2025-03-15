package dev.jdata.db.storage.backend.tabledata;

import java.util.Objects;

import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.Table;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.utils.adt.arrays.TwoDimensionalArray;
import dev.jdata.db.utils.checks.Checks;

public final class StorageTableSchemas {

    private final TwoDimensionalArray<StorageTableSchema> storageTableSchemasByTableId;

    public static StorageTableSchemas of(VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(versionedDatabaseSchemas);
        Objects.requireNonNull(numStorageBitsGetter);

        return new StorageTableSchemas(versionedDatabaseSchemas, numStorageBitsGetter);
    }

    private StorageTableSchemas(VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(versionedDatabaseSchemas);
        Objects.requireNonNull(numStorageBitsGetter);

        this.storageTableSchemasByTableId = new TwoDimensionalArray<>(versionedDatabaseSchemas.getNumKnownTables(), StorageTableSchema[][]::new, 10, StorageTableSchema[]::new);

        versionedDatabaseSchemas.forEachVersionedTable((i, t) -> {

            final StorageTableSchema storageTableSchema = new StorageTableSchema(t.getTable(), t.getSchemaVersion(), numStorageBitsGetter);

            storageTableSchemasByTableId.add(i, storageTableSchema);
        });
    }

    public synchronized void applyDatabaseShema(DatabaseSchema databaseSchema, NumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(numStorageBitsGetter);

        final DatabaseSchemaVersion databaseSchemaVersion = databaseSchema.getVersion();

        for (Table table : databaseSchema.getTables().getSchemaObjects()) {

            storageTableSchemasByTableId.addWithOuterExpand(table.getId(), new StorageTableSchema(table, databaseSchemaVersion, numStorageBitsGetter));
        }
    }

    public synchronized StorageTableSchema getStorageTableSchema(int tableId, DatabaseSchemaVersion databaseSchemaVersion) {

        Checks.isTableId(tableId);
        Objects.requireNonNull(databaseSchemaVersion);

        return storageTableSchemasByTableId.findExactlyOne(tableId, s -> s.getDatabaseSchemaVersion().equals(databaseSchemaVersion));
    }
}
