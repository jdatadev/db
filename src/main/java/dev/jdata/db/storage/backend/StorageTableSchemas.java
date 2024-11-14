package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.Table;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.utils.adt.arrays.TwoDimensionalArray;
import dev.jdata.db.utils.checks.Checks;

public final class StorageTableSchemas {

    private final TwoDimensionalArray<StorageTableSchema> storageTableSchemasByTableId;

    public StorageTableSchemas(VersionedDatabaseSchemas versionedDatabaseSchemas) {

        Objects.requireNonNull(versionedDatabaseSchemas);

        this.storageTableSchemasByTableId = new TwoDimensionalArray<>(versionedDatabaseSchemas.getNumKnownTables(), StorageTableSchema[][]::new, 10, StorageTableSchema[]::new);

        versionedDatabaseSchemas.forEachVersionedTable((i, t) -> {

            final StorageTableSchema storageTableSchema = new StorageTableSchema(t.getTable(), t.getSchemaVersion());

            storageTableSchemasByTableId.add(i, storageTableSchema);
        });
    }

    public synchronized void applyDatabaseShema(DatabaseSchema databaseSchema) {

        Objects.requireNonNull(databaseSchema);

        final DatabaseSchemaVersion databaseSchemaVersion = databaseSchema.getVersion();

        for (Table table : databaseSchema.getTables().getSchemaObjects()) {

            storageTableSchemasByTableId.addWithOuterExpand(table.getId(), new StorageTableSchema(table, databaseSchemaVersion));
        }
    }

    public synchronized StorageTableSchema getStorageTableSchema(int tableId, DatabaseSchemaVersion databaseSchemaVersion) {

        Checks.isTableId(tableId);
        Objects.requireNonNull(databaseSchemaVersion);

        return storageTableSchemasByTableId.findExactlyOne(tableId, s -> s.getDatabaseSchemaVersion().equals(databaseSchemaVersion));
    }
}
