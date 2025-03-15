package dev.jdata.db.storage.backend.tabledata.file;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.storage.backend.BaseStorageBackendFactory;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackendFactory;

public abstract class BaseTableDataStorageBackendFactory<C extends TableStorageBackendConfiguration>
        extends BaseStorageBackendFactory<C, TableDataStorageBackend>
        implements TableDataStorageBackendFactory<C> {

    protected abstract TableDataStorageBackend initializeTables(C configuration, StorageTableSchemas storageTableSchemas, NumStorageBitsGetter numStorageBitsGetter)
            throws IOException;

    @Override
    protected final TableDataStorageBackend initializeBackend(C configuration, StorageTableSchemas storageTableSchemas, NumStorageBitsGetter numStorageBitsGetter)
            throws IOException {

        Objects.requireNonNull(configuration);
        Objects.requireNonNull(storageTableSchemas);
        Objects.requireNonNull(numStorageBitsGetter);

        return initializeTables(configuration, storageTableSchemas, numStorageBitsGetter);
    }
}
