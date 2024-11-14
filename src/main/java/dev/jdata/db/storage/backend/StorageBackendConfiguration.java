package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.VersionedDatabaseSchemas;

public abstract class StorageBackendConfiguration {

    private final DatabaseSchema databaseSchema;
    private final VersionedDatabaseSchemas versionedDatabaseSchemas;
    private final NumStorageBitsParameters numStorageBitsParameters;

    protected StorageBackendConfiguration(DatabaseSchema databaseSchema, VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsParameters numStorageBitsParameters) {

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

    public final DatabaseSchema getDatabaseSchema() {
        return databaseSchema;
    }

    public final VersionedDatabaseSchemas getVersionedDatabaseSchemas() {
        return versionedDatabaseSchemas;
    }

    public final NumStorageBitsParameters getNumStorageBitsParameters() {
        return numStorageBitsParameters;
    }
}
