package dev.jdata.db.storage.backend;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.common.storagebits.ParameterizedNumStorageBitsGetter;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;

public abstract class BaseStorageBackendFactory<C extends StorageBackendConfiguration, T extends StorageBackend> implements StorageBackendFactory<C, T> {

    protected abstract T initializeBackend(C configuration, StorageTableSchemas storageTableSchemas, INumStorageBitsGetter numStorageBitsGetter) throws IOException;

    @Override
    public final T initialize(C configuration) throws IOException {

        Objects.requireNonNull(configuration);

        final INumStorageBitsGetter numStorageBitsGetter = getNumStorageBitsGetter(configuration);

        final StorageTableSchemas storageTableSchemas = StorageTableSchemas.of(configuration.getVersionedDatabaseSchemas(), numStorageBitsGetter);

        return initializeBackend(configuration, storageTableSchemas, numStorageBitsGetter);
    }

    private INumStorageBitsGetter getNumStorageBitsGetter(C configuration) {

        return new ParameterizedNumStorageBitsGetter(configuration.getNumStorageBitsParameters());
    }
}
