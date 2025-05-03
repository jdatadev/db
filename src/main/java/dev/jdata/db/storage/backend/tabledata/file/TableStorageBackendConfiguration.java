package dev.jdata.db.storage.backend.tabledata.file;

import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.configuration.CommonConfiguration;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.storage.backend.StorageBackendConfiguration;

public abstract class TableStorageBackendConfiguration extends StorageBackendConfiguration {

    protected TableStorageBackendConfiguration(CommonConfiguration commonConfiguration, IEffectiveDatabaseSchema databaseSchema,
            VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsParameters numStorageBitsParameters) {
        super(commonConfiguration, databaseSchema, versionedDatabaseSchemas, numStorageBitsParameters);
    }
}
