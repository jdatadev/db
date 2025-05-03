package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.configuration.CommonConfiguration;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;

public abstract class StorageBackendConfiguration extends CommonConfiguration {

    private final IEffectiveDatabaseSchema databaseSchema;
    private final VersionedDatabaseSchemas versionedDatabaseSchemas;
    private final NumStorageBitsParameters numStorageBitsParameters;

    protected StorageBackendConfiguration(CommonConfiguration commonConfiguration, IEffectiveDatabaseSchema databaseSchema, VersionedDatabaseSchemas versionedDatabaseSchemas,
            NumStorageBitsParameters numStorageBitsParameters) {
        super(commonConfiguration);

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(versionedDatabaseSchemas);

        if (!versionedDatabaseSchemas.containsVersion(databaseSchema.getVersion())) {

            throw new IllegalArgumentException();
        }

        Objects.requireNonNull(numStorageBitsParameters);

        this.databaseSchema = databaseSchema;
        this.versionedDatabaseSchemas = versionedDatabaseSchemas;
        this.numStorageBitsParameters = numStorageBitsParameters;
    }

    public final IEffectiveDatabaseSchema getDatabaseSchema() {
        return databaseSchema;
    }

    public final VersionedDatabaseSchemas getVersionedDatabaseSchemas() {
        return versionedDatabaseSchemas;
    }

    public final NumStorageBitsParameters getNumStorageBitsParameters() {
        return numStorageBitsParameters;
    }
}
