package dev.jdata.db.schema.storage;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.engine.database.strings.IStringCache;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.storage.sqloutputter.TextToByteOutputPrerequisites;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.IAbsoluteFileSystemAccess;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;

public final class DatabaseSchemaStorageFactory implements IDatabaseSchemaStorageFactory<IOException> {

    private static final String SCHEMA_VERSION_PREFIX = "schemaversion";

    private final IRelativeFileSystemAccess fileSystemAccess;
    private final IStringCache stringCache;
    private final TextToByteOutputPrerequisites textToByteOutputPrerequisites;

    private final NodeObjectCache<DatabaseSchemaStorage> storageCache;

    private DatabaseSchemaStorageFactory(AbsoluteDirectoryPath rootPath, IAbsoluteFileSystemAccess absoluteFileSystemAccess, IStringCache stringCache,
            TextToByteOutputPrerequisites textToByteOutputPrerequisites) {
        this(IRelativeFileSystemAccess.create(rootPath, absoluteFileSystemAccess), stringCache, textToByteOutputPrerequisites);
    }

    public DatabaseSchemaStorageFactory(IRelativeFileSystemAccess fileSystemAccess, IStringCache stringCache, TextToByteOutputPrerequisites textToByteOutputPrerequisites) {

        this.fileSystemAccess = Objects.requireNonNull(fileSystemAccess);
        this.stringCache = Objects.requireNonNull(stringCache);
        this.textToByteOutputPrerequisites = Objects.requireNonNull(textToByteOutputPrerequisites);

        this.storageCache = new NodeObjectCache<>(DatabaseSchemaStorage::new);
    }

    @Override
    public IDatabaseSchemaStorage<IOException> createSchemaDiffStorage(DatabaseSchemaVersion databaseSchemaVersion) throws IOException {

        Objects.requireNonNull(databaseSchemaVersion);

        final RelativeDirectoryPath directoryPath = constructSchemaVersionPath(fileSystemAccess, databaseSchemaVersion, stringCache);

        fileSystemAccess.createDirectory(directoryPath);

        final NodeObjectCache<DatabaseSchemaStorage> cache = storageCache;

        final DatabaseSchemaStorage storage;

        synchronized (cache) {

            storage = storageCache.allocate();
        }

        storage.initialize(directoryPath, this);

        return storage;
    }

    void onSchemaComplete(DatabaseSchemaStorage storage) {

        final NodeObjectCache<DatabaseSchemaStorage> cache = storageCache;

        storage.reset();

        synchronized (cache) {

            cache.free(storage);
        }
    }

    IRelativeFileSystemAccess getFileSystemAccess() {
        return fileSystemAccess;
    }

    TextToByteOutputPrerequisites getTextToByteOutputPrerequisites() {
        return textToByteOutputPrerequisites;
    }

    private static RelativeDirectoryPath constructSchemaVersionPath(IRelativeFileSystemAccess fileSystemAccess, DatabaseSchemaVersion databaseSchemaVersion,
            IStringCache stringCache) {

        final int schemaVersionNumber = databaseSchemaVersion.getVersionNumber();
        final String schemaVersionNumberString = stringCache.getOrAddString(SCHEMA_VERSION_PREFIX + schemaVersionNumber);

        return fileSystemAccess.directoryPathOf(schemaVersionNumberString);
    }
}
