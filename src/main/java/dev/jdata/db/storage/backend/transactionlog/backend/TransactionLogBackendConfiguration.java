package dev.jdata.db.storage.backend.transactionlog.backend;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.NumStorageBitsParameters;
import dev.jdata.db.storage.backend.StorageBackendConfiguration;

public abstract class TransactionLogBackendConfiguration extends StorageBackendConfiguration {

    protected TransactionLogBackendConfiguration(DatabaseSchema databaseSchema, VersionedDatabaseSchemas versionedDatabaseSchemas,
            NumStorageBitsParameters numStorageBitsParameters) {
        super(databaseSchema, versionedDatabaseSchemas, numStorageBitsParameters);
    }
}
