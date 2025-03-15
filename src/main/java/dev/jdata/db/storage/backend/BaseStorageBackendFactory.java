package dev.jdata.db.storage.backend;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.common.storagebits.ParameterizedNumStorageBitsGetter;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;

public abstract class BaseStorageBackendFactory<C extends StorageBackendConfiguration, T extends StorageBackend> implements StorageBackendFactory<C, T> {

    protected abstract T initializeBackend(C configuration, StorageTableSchemas storageTableSchemas, NumStorageBitsGetter numStorageBitsGetter) throws IOException;

    @Override
    public final T initialize(C configuration) throws IOException {

        Objects.requireNonNull(configuration);

        final NumStorageBitsGetter numStorageBitsGetter = getNumStorageBitsGetter(configuration);

        final StorageTableSchemas storageTableSchemas = StorageTableSchemas.of(configuration.getVersionedDatabaseSchemas(), numStorageBitsGetter);

        return initializeBackend(configuration, storageTableSchemas, numStorageBitsGetter);
    }

    private NumStorageBitsGetter getNumStorageBitsGetter(C configuration) {

        return new ParameterizedNumStorageBitsGetter(configuration.getNumStorageBitsParameters());
    }
}
