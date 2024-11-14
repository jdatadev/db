package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.VersionedDatabaseSchemas;

public abstract class StorageBackendConfiguration {

    private final DatabaseSchema databaseSchema;
    private final VersionedDatabaseSchemas versionedDatabaseSchemas;

    protected StorageBackendConfiguration(DatabaseSchema databaseSchema, VersionedDatabaseSchemas versionedDatabaseSchemas) {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(versionedDatabaseSchemas);

        if (!versionedDatabaseSchemas.containsVersion(databaseSchema.getVersion())) {

            throw new IllegalArgumentException();
        }

        this.databaseSchema = databaseSchema;
        this.versionedDatabaseSchemas = versionedDatabaseSchemas;
    }

    public final DatabaseSchema getDatabaseSchema() {
        return databaseSchema;
    }

    public final VersionedDatabaseSchemas getVersionedDatabaseSchemas() {
        return versionedDatabaseSchemas;
    }
}
