package dev.jdata.db.storage.backend.tabledata.file.simple;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.file.BaseFileTableDataStorageBackendFactory;
import dev.jdata.db.storage.backend.tabledata.file.FileTableStorageFile;
import dev.jdata.db.storage.backend.tabledata.file.FileTableStorageFiles;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.file.access.RelativeFilePath;

public final class SimpleFileTableDataStorageBackendFactory extends BaseFileTableDataStorageBackendFactory {

    public SimpleFileTableDataStorageBackendFactory(ICachedIndexListAllocator<FileTableStorageFile> storageFileIndexListAllocator,
            ICachedIndexListAllocator<RelativeFilePath> relativeFilePathIndexListAllocator) {
        super(storageFileIndexListAllocator, relativeFilePathIndexListAllocator);
    }

    @Override
    protected TableDataStorageBackend createTableStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, INumStorageBitsGetter numStorageBitsGetter,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId) {

        return new SimpleFileTableDataStorageBackend(versionedDatabaseSchemas, numStorageBitsGetter, fileTableStorageByTableId);
    }
}
