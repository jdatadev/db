package dev.jdata.db.storage.backend.tabledata;

import dev.jdata.db.storage.backend.StorageBackendFactory;
import dev.jdata.db.storage.backend.tabledata.file.TableStorageBackendConfiguration;

public interface TableDataStorageBackendFactory<C extends TableStorageBackendConfiguration> extends StorageBackendFactory<C, TableDataStorageBackend> {

}
