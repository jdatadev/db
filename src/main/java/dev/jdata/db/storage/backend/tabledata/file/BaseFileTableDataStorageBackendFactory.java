package dev.jdata.db.storage.backend.tabledata.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.SchemaMapGetters;
import dev.jdata.db.schema.Table;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema.StorageTableFileSchemaGetters;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.file.access.RelativeFileSystemAccess;

public abstract class BaseFileTableDataStorageBackendFactory extends BaseTableDataStorageBackendFactory<FileTableStorageBackendConfiguration> {

    protected abstract TableDataStorageBackend createTableStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsGetter numStorageBitsGetter,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId);

    @Override
    protected TableDataStorageBackend initializeTables(FileTableStorageBackendConfiguration configuration, StorageTableSchemas storageTableSchemas,
            NumStorageBitsGetter numStorageBitsGetter) throws IOException {

        Objects.requireNonNull(configuration);
        Objects.requireNonNull(storageTableSchemas);

        final TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId = initializeTableDirectories(configuration, storageTableSchemas);

        return createTableStorageBackend(configuration.getVersionedDatabaseSchemas(), numStorageBitsGetter, fileTableStorageByTableId);
    }

    private static TableByIdMap<FileTableStorageFiles> initializeTableDirectories(FileTableStorageBackendConfiguration configuration, StorageTableSchemas storageTableSchemas)
            throws IOException {

        final DatabaseSchema databaseSchema = configuration.getDatabaseSchema();
        final VersionedDatabaseSchemas versionedDatabaseSchemas = configuration.getVersionedDatabaseSchemas();

        final AbsoluteDirectoryPath rootPath = configuration.getRootPath();
        final RelativeFileSystemAccess fileSystemAccess = RelativeFileSystemAccess.create(rootPath, configuration.getFileSystemAccess());

        applyDatabaseSchema(fileSystemAccess, rootPath, databaseSchema);

        final StorageTableFileSchemaGetters storageTableFileSchemaGetters = new StorageTableFileSchemaGetters(versionedDatabaseSchemas, storageTableSchemas);

        return new TableByIdMap<>(databaseSchema, FileTableStorageFiles[]::new, t -> {

            final RelativeDirectoryPath tableFilePath = getTablePath(rootPath, t.getName());

            final List<RelativeFilePath> tableFilePaths = fileSystemAccess.listFilePaths(tableFilePath);

            final List<FileTableStorageFile> fileTableStorageFileList = readTableFiles(fileSystemAccess, tableFilePaths, storageTableFileSchemaGetters);

            return new FileTableStorageFiles(fileSystemAccess, tableFilePath, fileTableStorageFileList);
        });
    }

    private static List<FileTableStorageFile> readTableFiles(RelativeFileSystemAccess fileSystemAccess, List<RelativeFilePath> tableFilePaths,
            StorageTableFileSchemaGetters storageTableFileSchemaGetters) throws IOException {

        final List<FileTableStorageFile> fileTableStorageFileList = new ArrayList<>(tableFilePaths.size());

        for (RelativeFilePath tableFilePath : tableFilePaths) {

            final FileTableStorageFile fileTableStorageFile = FileTableStorageFile.openExistingFile(fileSystemAccess, tableFilePath, storageTableFileSchemaGetters);

            fileTableStorageFileList.add(fileTableStorageFile);
        }

        return fileTableStorageFileList;
    }

    private static void applyDatabaseSchema(RelativeFileSystemAccess fileSystemAccess, AbsoluteDirectoryPath rootPath, DatabaseSchema databaseSchema) throws IOException {

        Objects.requireNonNull(fileSystemAccess);
        Objects.requireNonNull(rootPath);
        Objects.requireNonNull(databaseSchema);

        final SchemaMapGetters<Table> tableMap = databaseSchema.getTables();

        for (String schemaName : tableMap.getSchemaNames()) {

            final RelativeDirectoryPath tableDirectoryPath = rootPath.relativize(rootPath.resolveDirectory(schemaName));

            if (fileSystemAccess.exists(tableDirectoryPath)) {

                if (!fileSystemAccess.isDirectory(tableDirectoryPath)) {

                    throw new IllegalStateException();
                }
            }
            else {
                fileSystemAccess.createDirectories(tableDirectoryPath);
            }
        }
    }

    private static RelativeDirectoryPath getTablePath(AbsoluteDirectoryPath rootPath, String tableName) {

        return rootPath.relativize(rootPath.resolveDirectory(tableName));
    }
}
