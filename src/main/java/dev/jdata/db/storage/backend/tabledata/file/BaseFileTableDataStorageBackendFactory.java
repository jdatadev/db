package dev.jdata.db.storage.backend.tabledata.file;

import java.io.IOException;
import java.util.Objects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBNamedObject;
import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.configuration.CommonConfiguration;
import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema.StorageTableFileSchemaGetters;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;

public abstract class BaseFileTableDataStorageBackendFactory extends BaseTableDataStorageBackendFactory<FileTableStorageBackendConfiguration> {

    protected abstract TableDataStorageBackend createTableStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, INumStorageBitsGetter numStorageBitsGetter,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId);

    private final ICachedIndexListAllocator<FileTableStorageFile> storageFileIndexListAllocator;
    private final ICachedIndexListAllocator<RelativeFilePath> relativeFilePathIndexListAllocator;

    protected BaseFileTableDataStorageBackendFactory(ICachedIndexListAllocator<FileTableStorageFile> storageFileIndexListAllocator,
            ICachedIndexListAllocator<RelativeFilePath> relativeFilePathIndexListAllocator) {

        this.storageFileIndexListAllocator = Objects.requireNonNull(storageFileIndexListAllocator);
        this.relativeFilePathIndexListAllocator = Objects.requireNonNull(relativeFilePathIndexListAllocator);
    }

    @Override
    protected TableDataStorageBackend initializeTables(FileTableStorageBackendConfiguration configuration, StorageTableSchemas storageTableSchemas,
            INumStorageBitsGetter numStorageBitsGetter) throws IOException {

        Objects.requireNonNull(configuration);
        Objects.requireNonNull(storageTableSchemas);

        final TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId = initializeTableDirectories(configuration, storageTableSchemas, storageFileIndexListAllocator,
                relativeFilePathIndexListAllocator);

        return createTableStorageBackend(configuration.getVersionedDatabaseSchemas(), numStorageBitsGetter, fileTableStorageByTableId);
    }

    private static TableByIdMap<FileTableStorageFiles> initializeTableDirectories(FileTableStorageBackendConfiguration configuration, StorageTableSchemas storageTableSchemas,
            ICachedIndexListAllocator<FileTableStorageFile> storageFileIndexListAllocator, ICachedIndexListAllocator<RelativeFilePath> relativeFilePathIndexListAllocator)
            throws IOException {

        final IEffectiveDatabaseSchema databaseSchema = configuration.getDatabaseSchema();
        final VersionedDatabaseSchemas versionedDatabaseSchemas = configuration.getVersionedDatabaseSchemas();

        final AbsoluteDirectoryPath rootPath = configuration.getRootPath();
        final IRelativeFileSystemAccess fileSystemAccess = IRelativeFileSystemAccess.create(rootPath, configuration.getFileSystemAccess());

        final DirectoryResolveParameters directoryResolveParameters = new DirectoryResolveParameters();

        final RelativeDirectoryPath relativeRootPath = RelativeDirectoryPath.ROOT;

        directoryResolveParameters.initialize(fileSystemAccess, relativeRootPath, configuration.getStringResolver());

        applyDatabaseSchema(directoryResolveParameters, databaseSchema);

        final StorageTableFileSchemaGetters storageTableFileSchemaGetters = new StorageTableFileSchemaGetters(versionedDatabaseSchemas, storageTableSchemas);

        return new TableByIdMap<>(databaseSchema, FileTableStorageFiles[]::new,
                t -> createFileTableStorageFiles(fileSystemAccess, relativeRootPath, t, configuration, storageTableFileSchemaGetters, storageFileIndexListAllocator,
                        relativeFilePathIndexListAllocator));
    }

    private static FileTableStorageFiles createFileTableStorageFiles(IRelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath rootPath, Table table,
            CommonConfiguration configuration, StorageTableFileSchemaGetters storageTableFileSchemaGetters,
            ICachedIndexListAllocator<FileTableStorageFile> storageFileIndexListAllocator, ICachedIndexListAllocator<RelativeFilePath> relativeFilePathIndexListAllocator)
                    throws IOException {

        final FileTableStorageFiles result;

        final RelativeDirectoryPath tableFilePath = getTablePath(fileSystemAccess, rootPath, table, configuration);

        ICachedIndexList<RelativeFilePath> tableFilePaths = null;

        final ICachedIndexListBuilder<RelativeFilePath> tableFilePathsBuilder = relativeFilePathIndexListAllocator.createBuilder();

        try {
            fileSystemAccess.listFilePaths(tableFilePath, tableFilePathsBuilder, (p, b) -> b.addTail(p));

            tableFilePaths = tableFilePathsBuilder.buildOrEmpty();

            ICachedIndexList<FileTableStorageFile> fileTableStorageFileList = null;

            try {
                fileTableStorageFileList = readTableFiles(fileSystemAccess, tableFilePaths, storageTableFileSchemaGetters, storageFileIndexListAllocator);

                result = new FileTableStorageFiles(fileSystemAccess, tableFilePath, fileTableStorageFileList, null);
            }
            finally {

                if (fileTableStorageFileList != null) {

                    storageFileIndexListAllocator.freeImmutable(fileTableStorageFileList);
                }
            }
        }
        finally {

            if (tableFilePaths != null) {

                relativeFilePathIndexListAllocator.freeImmutable(tableFilePaths);
            }

            relativeFilePathIndexListAllocator.freeBuilder(tableFilePathsBuilder);
        }

        return result;
    }

    private static ICachedIndexList<FileTableStorageFile> readTableFiles(IRelativeFileSystemAccess fileSystemAccess, IIndexList<RelativeFilePath> tableFilePaths,
            StorageTableFileSchemaGetters storageTableFileSchemaGetters, ICachedIndexListAllocator<FileTableStorageFile> storageFileIndexListAllocator) throws IOException {

        final ICachedIndexList<FileTableStorageFile> result;

        final long numElements = tableFilePaths.getNumElements();

        final int initialCapacity = IOnlyElementsView.intNumElements(tableFilePaths);

        final ICachedIndexListBuilder<FileTableStorageFile> storageFileListBuilder = storageFileIndexListAllocator.createBuilder(initialCapacity);

        try {
            for (long i = 0L; i < numElements; ++ i) {

                final RelativeFilePath tableFilePath = tableFilePaths.get(i);

                final FileTableStorageFile fileTableStorageFile = FileTableStorageFile.openExistingFile(fileSystemAccess, tableFilePath, storageTableFileSchemaGetters);

                storageFileListBuilder.addTail(fileTableStorageFile);
            }

            result = storageFileListBuilder.buildOrEmpty();
        }
        finally {

            storageFileIndexListAllocator.freeBuilder(storageFileListBuilder);
        }

        return result;
    }

    static final class DirectoryResolveParameters {

        private IRelativeFileSystemAccess fileSystemAccess;
        private RelativeDirectoryPath rootPath;
        private StringResolver stringResolver;

        void initialize(IRelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath rootPath, StringResolver stringResolver) {

            this.fileSystemAccess = Objects.requireNonNull(fileSystemAccess);
            this.rootPath = Objects.requireNonNull(rootPath);
            this.stringResolver = Objects.requireNonNull(stringResolver);
        }
    }

    private static void applyDatabaseSchema(DirectoryResolveParameters parameters, IEffectiveDatabaseSchema databaseSchema) throws IOException {

        Objects.requireNonNull(parameters);

        final ISchemaMap<Table> tableMap = databaseSchema.getTableMap();

        tableMap.forEach(parameters, (o, p) -> {

            final IRelativeFileSystemAccess fileSystemAccess = p.fileSystemAccess;

            final String tableName = getFileSystemName(o, p.stringResolver);

            final RelativeDirectoryPath tableDirectoryPath = fileSystemAccess.resolveDirectory(p.rootPath, tableName);

            if (fileSystemAccess.exists(tableDirectoryPath)) {

                if (!fileSystemAccess.isDirectory(tableDirectoryPath)) {

                    throw new IllegalStateException();
                }
            }
            else {
                fileSystemAccess.createDirectories(tableDirectoryPath);
            }
        });
    }

    private static RelativeDirectoryPath getTablePath(IRelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath rootPath, Table table,
            CommonConfiguration configuration) {

        final String tableName = getFileSystemName(table, configuration.getStringResolver());

        return fileSystemAccess.resolveDirectory(rootPath, tableName);
    }

    private static final String getFileSystemName(DBNamedObject dbNamedObject, StringResolver stringResolver) {

        return stringResolver.asString(dbNamedObject.getFileSystemName());
    }
}
