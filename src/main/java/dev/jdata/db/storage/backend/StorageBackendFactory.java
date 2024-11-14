package dev.jdata.db.storage.backend;

import java.io.IOException;

public interface StorageBackendFactory<C extends StorageBackendConfiguration, T extends StorageBackend> {

    T initialize(C configuration) throws IOException;
}
