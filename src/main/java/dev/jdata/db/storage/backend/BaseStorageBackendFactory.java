package dev.jdata.db.storage.backend;

import java.io.IOException;
import java.util.Objects;

public abstract class BaseStorageBackendFactory<C extends StorageBackendConfiguration, T extends StorageBackend> implements StorageBackendFactory<C, T> {

    protected abstract T initializeBackend(C configuration, StorageTableSchemas storageTableSchemas) throws IOException;

    @Override
    public final T initialize(C configuration) throws IOException {

        Objects.requireNonNull(configuration);

        final StorageTableSchemas storageTableSchemas = new StorageTableSchemas(configuration.getVersionedDatabaseSchemas());

        return initializeBackend(configuration, storageTableSchemas);
    }
}
