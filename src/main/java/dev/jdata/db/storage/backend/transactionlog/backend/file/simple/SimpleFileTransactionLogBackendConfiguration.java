package dev.jdata.db.storage.backend.transactionlog.backend.file.simple;

import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.configuration.CommonConfiguration;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogBackendConfiguration;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.IAbsoluteFileSystemAccess;

public final class SimpleFileTransactionLogBackendConfiguration extends FileTransactionLogBackendConfiguration {

    public SimpleFileTransactionLogBackendConfiguration(CommonConfiguration commonConfiguration, IEffectiveDatabaseSchema databaseSchema,
            VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsParameters numStorageBitsParameters, IAbsoluteFileSystemAccess fileSystemAccess,
            AbsoluteDirectoryPath rootPath) {
        super(commonConfiguration, databaseSchema, versionedDatabaseSchemas, numStorageBitsParameters, fileSystemAccess, rootPath);
    }
}
