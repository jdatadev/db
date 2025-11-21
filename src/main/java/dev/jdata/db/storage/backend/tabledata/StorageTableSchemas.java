package dev.jdata.db.storage.backend.tabledata;

import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.diff.IDiffDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.arrays.TwoDimensionalArray;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public final class StorageTableSchemas {

    private final TwoDimensionalArray<StorageTableSchema> storageTableSchemasByTableId;

    public static StorageTableSchemas of(VersionedDatabaseSchemas versionedDatabaseSchemas, INumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(versionedDatabaseSchemas);
        Objects.requireNonNull(numStorageBitsGetter);

        return new StorageTableSchemas(versionedDatabaseSchemas, numStorageBitsGetter);
    }

    private StorageTableSchemas(VersionedDatabaseSchemas versionedDatabaseSchemas, INumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(versionedDatabaseSchemas);
        Objects.requireNonNull(numStorageBitsGetter);

        this.storageTableSchemasByTableId = new TwoDimensionalArray<>(AllocationType.HEAP, versionedDatabaseSchemas.getNumKnownTables(), StorageTableSchema[][]::new, 10,
                StorageTableSchema[]::new);

        versionedDatabaseSchemas.forEachVersionedTable((i, t) -> {

            final StorageTableSchema storageTableSchema = new StorageTableSchema(t.getTable(), t.getSchemaVersion(), numStorageBitsGetter);

            storageTableSchemasByTableId.add(i, storageTableSchema);
        });
    }

    public synchronized void applyDatabaseShema(IEffectiveDatabaseSchema effectiveDatabaseSchema, IDiffDatabaseSchema diffDatabaseSchema, INumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(effectiveDatabaseSchema);
        Objects.requireNonNull(diffDatabaseSchema);
        Objects.requireNonNull(numStorageBitsGetter);

        final DatabaseSchemaVersion databaseSchemaVersion = effectiveDatabaseSchema.getVersion();

        final IIndexList<Table> tables = effectiveDatabaseSchema.getTables();

        final long numElements = tables.getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            final Table table = tables.get(i);

            storageTableSchemasByTableId.addWithOuterExpand(table.getId(), new StorageTableSchema(table, databaseSchemaVersion, numStorageBitsGetter));
        }
    }

    public synchronized StorageTableSchema getStorageTableSchema(int tableId, DatabaseSchemaVersion databaseSchemaVersion) {

        Checks.isTableId(tableId);
        Objects.requireNonNull(databaseSchemaVersion);

        return storageTableSchemasByTableId.findExactlyOne(tableId, s -> s.getDatabaseSchemaVersion().equals(databaseSchemaVersion));
    }
}
