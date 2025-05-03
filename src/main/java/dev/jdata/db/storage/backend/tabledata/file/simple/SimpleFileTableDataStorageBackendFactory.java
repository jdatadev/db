package dev.jdata.db.storage.backend.tabledata.file.simple;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.file.BaseFileTableDataStorageBackendFactory;
import dev.jdata.db.storage.backend.tabledata.file.FileTableStorageFiles;

public final class SimpleFileTableDataStorageBackendFactory extends BaseFileTableDataStorageBackendFactory {

    @Override
    protected TableDataStorageBackend createTableStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, INumStorageBitsGetter numStorageBitsGetter,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId) {

        return new SimpleFileTableDataStorageBackend(versionedDatabaseSchemas, numStorageBitsGetter, fileTableStorageByTableId);
    }
}
