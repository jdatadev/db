package dev.jdata.db.storage.backend.tabledata.file;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.storage.backend.BaseStorageBackendFactory;
import dev.jdata.db.storage.backend.StorageTableSchemas;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackendFactory;

public abstract class BaseTableDataStorageBackendFactory<C extends TableStorageBackendConfiguration>
        extends BaseStorageBackendFactory<C, TableDataStorageBackend>
        implements TableDataStorageBackendFactory<C> {

    protected abstract TableDataStorageBackend initializeTables(C configuration, StorageTableSchemas storageTableSchemas) throws IOException;

    @Override
    protected final TableDataStorageBackend initializeBackend(C configuration, StorageTableSchemas storageTableSchemas) throws IOException {

        Objects.requireNonNull(configuration);
        Objects.requireNonNull(storageTableSchemas);

        return initializeTables(configuration, storageTableSchemas);
    }
}
