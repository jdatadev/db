package dev.jdata.db.storage.backend.transactionlog.backend.file;

import java.util.Objects;

import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.transactionlog.backend.TransactionLogBackendConfiguration;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.AbsoluteFileSystemAccess;

public final class FileTransactionLogBackendConfiguration extends TransactionLogBackendConfiguration {

    private final AbsoluteFileSystemAccess fileSystemAccess;
    private final AbsoluteDirectoryPath rootPath;

    public FileTransactionLogBackendConfiguration(DatabaseSchema databaseSchema, VersionedDatabaseSchemas versionedDatabaseSchemas,
            NumStorageBitsParameters numStorageBitsParameters, AbsoluteFileSystemAccess fileSystemAccess, AbsoluteDirectoryPath rootPath) {
        super(databaseSchema, versionedDatabaseSchemas, numStorageBitsParameters);

        this.fileSystemAccess = Objects.requireNonNull(fileSystemAccess);
        this.rootPath = Objects.requireNonNull(rootPath);
    }

    public AbsoluteFileSystemAccess getFileSystemAccess() {
        return fileSystemAccess;
    }

    public AbsoluteDirectoryPath getRootPath() {
        return rootPath;
    }
}
