package dev.jdata.db.storage.backend.transactionlog.backend;

import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.configuration.CommonConfiguration;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.storage.backend.StorageBackendConfiguration;

public abstract class TransactionLogBackendConfiguration extends StorageBackendConfiguration {

    protected TransactionLogBackendConfiguration(CommonConfiguration commonConfiguration, IEffectiveDatabaseSchema databaseSchema,
            VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsParameters numStorageBitsParameters) {
        super(commonConfiguration, databaseSchema, versionedDatabaseSchemas, numStorageBitsParameters);
    }
}
