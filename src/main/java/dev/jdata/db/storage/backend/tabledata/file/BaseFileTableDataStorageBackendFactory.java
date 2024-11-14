package dev.jdata.db.storage.backend.tabledata.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.SchemaMapGetters;
import dev.jdata.db.schema.Table;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.StorageTableFileSchema;
import dev.jdata.db.storage.backend.StorageTableFileSchema.NumNullValueBitMapBitsGetter;
import dev.jdata.db.storage.backend.StorageTableFileSchema.StorageTableSchemaGetter;
import dev.jdata.db.storage.backend.StorageTableSchemas;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.utils.paths.PathIOUtil;

public abstract class BaseFileTableDataStorageBackendFactory extends BaseTableDataStorageBackendFactory<FileTableStorageBackendConfiguration> {

    protected abstract TableDataStorageBackend createTableStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId);

    @Override
    protected TableDataStorageBackend initializeTables(FileTableStorageBackendConfiguration configuration, StorageTableSchemas storageTableSchemas) throws IOException {

        Objects.requireNonNull(configuration);
        Objects.requireNonNull(storageTableSchemas);

        final TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId = initializeTableDirectories(configuration, storageTableSchemas);

        return createTableStorageBackend(configuration.getVersionedDatabaseSchemas(), fileTableStorageByTableId);
    }

    private static TableByIdMap<FileTableStorageFiles> initializeTableDirectories(FileTableStorageBackendConfiguration configuration, StorageTableSchemas storageTableSchemas)
            throws IOException {

        final DatabaseSchema databaseSchema = configuration.getDatabaseSchema();
        final VersionedDatabaseSchemas versionedDatabaseSchemas = configuration.getVersionedDatabaseSchemas();
        final Path rootPath = configuration.getRootPath();

        applyDatabaseSchema(rootPath, databaseSchema);

        final NumNullValueBitMapBitsGetter numNullValueBitMapBitsGetter = (t, v) -> getNumNullValueBitmapBits(t, v, versionedDatabaseSchemas);

        return new TableByIdMap<>(databaseSchema, FileTableStorageFiles[]::new, t -> {

            final Path tableFilePath = getTablePath(rootPath, t.getName());

            final List<Path> tableFilePaths = PathIOUtil.listFilePaths(tableFilePath);

            final List<FileTableStorageFile> fileTableStorageFileList = readTableFiles(tableFilePaths, storageTableSchemas::getStorageTableSchema, numNullValueBitMapBitsGetter);

            return new FileTableStorageFiles(tableFilePath, fileTableStorageFileList);
        });
    }

    private static List<FileTableStorageFile> readTableFiles(List<Path> tableFilePaths, StorageTableSchemaGetter storageTableSchemaGetter,
            NumNullValueBitMapBitsGetter numNullValueBitMapBitsGetter) throws IOException {

        final List<FileTableStorageFile> fileTableStorageFileList = new ArrayList<>(tableFilePaths.size());

        for (Path tableFilePath : tableFilePaths) {

            final FileTableStorageFile fileTableStorageFile = readTableFile(tableFilePath, storageTableSchemaGetter, numNullValueBitMapBitsGetter);

            fileTableStorageFileList.add(fileTableStorageFile);
        }

        return fileTableStorageFileList;
    }

    private static FileTableStorageFile readTableFile(Path path, StorageTableSchemaGetter storageTableSchemaGetter, NumNullValueBitMapBitsGetter numNullValueBitMapBitsGetter)
            throws IOException {

        final RandomAccessFile randomAccessFile = FileTableStorageFiles.createRandomAccessFile(path);

        final StorageTableFileSchema storageTableSchema = StorageTableFileSchema.read(randomAccessFile, storageTableSchemaGetter, numNullValueBitMapBitsGetter);

        final long startRow = randomAccessFile.readLong();

        final long tableFileOffset = randomAccessFile.getFilePointer();
        final long tableFileLength = randomAccessFile.length();

        final long tableFileDataLength = tableFileLength - tableFileOffset;

        final int numBitsPerByte = 8;

        final long tableFileDataBits = tableFileDataLength * numBitsPerByte;

        final int totalNumRowBits = storageTableSchema.getTotalNumRowBits();

        final long numRows = tableFileDataBits / totalNumRowBits;

        final long numLeftOverBits = totalNumRowBits - (numRows * totalNumRowBits);

        if (numLeftOverBits >= numBitsPerByte) {

            throw new IllegalStateException();
        }

        return new FileTableStorageFile(path, storageTableSchema, randomAccessFile, startRow);
    }

    private static int getNumNullValueBitmapBits(int tableId, DatabaseSchemaVersion databaseSchemaVersion, VersionedDatabaseSchemas versionedDatabaseSchemas) {

        final DatabaseSchema databaseSchema = versionedDatabaseSchemas.getSchema(databaseSchemaVersion);

        final Table table = databaseSchema.getTable(tableId);

        return table.getNumNullableColumns();
    }

    private static void applyDatabaseSchema(Path rootPath, DatabaseSchema databaseSchema) throws IOException {

        Objects.requireNonNull(rootPath);
        Objects.requireNonNull(databaseSchema);

        final SchemaMapGetters<Table> tableMap = databaseSchema.getTables();

        for (String schemaName : tableMap.getSchemaNames()) {

            final Path tableDirectoryPath = rootPath.resolve(schemaName);

            if (Files.exists(tableDirectoryPath)) {

                if (!Files.isDirectory(tableDirectoryPath)) {

                    throw new IllegalStateException();
                }
            }
            else {
                Files.createDirectories(tableDirectoryPath);
            }
        }
    }

    private static Path getTablePath(Path rootPath, String tableName) {

        return rootPath.resolve(tableName);
    }
}
