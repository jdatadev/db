package dev.jdata.db.storage.backend.tabledata;

import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.schema.VersionedDatabaseSchemas;

public abstract class BaseTableDataStorageBackend implements TableDataStorageBackend {

    private final StorageTableSchemas storageTableSchemas;

    protected BaseTableDataStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, INumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(versionedDatabaseSchemas);
        Objects.requireNonNull(numStorageBitsGetter);

        this.storageTableSchemas = StorageTableSchemas.of(versionedDatabaseSchemas, numStorageBitsGetter);
    }

    protected final StorageTableSchemas getStorageTableSchemas() {
        return storageTableSchemas;
    }
}
