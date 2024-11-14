package dev.jdata.db.storage.backend.tabledata.file;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.NumStorageBitsParameters;
import dev.jdata.db.storage.backend.StorageBackendConfiguration;

public abstract class TableStorageBackendConfiguration extends StorageBackendConfiguration {

    protected TableStorageBackendConfiguration(DatabaseSchema databaseSchema, VersionedDatabaseSchemas versionedDatabaseSchemas,
            NumStorageBitsParameters numStorageBitsParameters) {
        super(databaseSchema, versionedDatabaseSchemas, numStorageBitsParameters);
    }
}
