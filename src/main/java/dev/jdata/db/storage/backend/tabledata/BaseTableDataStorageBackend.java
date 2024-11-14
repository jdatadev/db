package dev.jdata.db.storage.backend.tabledata;

import java.util.Objects;

import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.StorageTableSchemas;

public abstract class BaseTableDataStorageBackend implements TableDataStorageBackend {

    private final StorageTableSchemas storageTableSchemas;

    protected BaseTableDataStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas) {

        Objects.requireNonNull(versionedDatabaseSchemas);

        this.storageTableSchemas = new StorageTableSchemas(versionedDatabaseSchemas);
    }

    protected final StorageTableSchemas getStorageTableSchemas() {
        return storageTableSchemas;
    }
}
