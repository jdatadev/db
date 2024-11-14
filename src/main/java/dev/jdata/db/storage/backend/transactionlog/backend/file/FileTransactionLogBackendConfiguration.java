package dev.jdata.db.storage.backend.transactionlog.backend.file;

import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.transactionlog.backend.TransactionLogBackendConfiguration;

public final class FileTransactionLogBackendConfiguration extends TransactionLogBackendConfiguration {

    private final Path rootPath;

    public FileTransactionLogBackendConfiguration(DatabaseSchema databaseSchema, VersionedDatabaseSchemas versionedDatabaseSchemas, Path rootPath) {
        super(databaseSchema, versionedDatabaseSchemas);

        this.rootPath = Objects.requireNonNull(rootPath);
    }

    public Path getRootPath() {
        return rootPath;
    }
}
