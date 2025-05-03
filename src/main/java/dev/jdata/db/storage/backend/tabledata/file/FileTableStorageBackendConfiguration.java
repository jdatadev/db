package dev.jdata.db.storage.backend.tabledata.file;

import java.util.Objects;

import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.configuration.CommonConfiguration;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.IAbsoluteFileSystemAccess;

public final class FileTableStorageBackendConfiguration extends TableStorageBackendConfiguration {

    private final IAbsoluteFileSystemAccess fileSystemAccess;
    private final AbsoluteDirectoryPath rootPath;

    public FileTableStorageBackendConfiguration(CommonConfiguration commonConfiguration, IEffectiveDatabaseSchema databaseSchema,
            VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsParameters numStorageBitsParameters, IAbsoluteFileSystemAccess fileSystemAccess,
            AbsoluteDirectoryPath rootPath) {
        super(commonConfiguration, databaseSchema, versionedDatabaseSchemas, numStorageBitsParameters);

        this.fileSystemAccess = Objects.requireNonNull(fileSystemAccess);
        this.rootPath = Objects.requireNonNull(rootPath);
    }

    public IAbsoluteFileSystemAccess getFileSystemAccess() {
        return fileSystemAccess;
    }

    public AbsoluteDirectoryPath getRootPath() {
        return rootPath;
    }
}
