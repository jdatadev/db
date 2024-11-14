package dev.jdata.db.storage.backend.tabledata.file;

import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.VersionedDatabaseSchemas;

public final class FileTableStorageBackendConfiguration extends TableStorageBackendConfiguration {

    private final Path rootPath;

    public FileTableStorageBackendConfiguration(DatabaseSchema databaseSchema, VersionedDatabaseSchemas versionedDatabaseSchemas, Path rootPath) {
        super(databaseSchema, versionedDatabaseSchemas);

        this.rootPath = Objects.requireNonNull(rootPath);
    }

    public Path getRootPath() {
        return rootPath;
    }
}
