package dev.jdata.db.storage.backend.tabledata.file.bitcompressed;

import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.VersionedDatabaseSchemas;
import dev.jdata.db.storage.backend.tabledata.TableDataStorageBackend;
import dev.jdata.db.storage.backend.tabledata.file.BaseFileTableDataStorageBackendFactory;
import dev.jdata.db.storage.backend.tabledata.file.FileTableStorageFiles;

public final class BitCompressedFileTableDataStorageBackendFactory extends BaseFileTableDataStorageBackendFactory {

    @Override
    protected TableDataStorageBackend createTableStorageBackend(VersionedDatabaseSchemas versionedDatabaseSchemas, NumStorageBitsGetter numStorageBitsGetter,
            TableByIdMap<FileTableStorageFiles> fileTableStorageByTableId) {

        return new BitCompressedFileTableStorageBackend(versionedDatabaseSchemas, numStorageBitsGetter, fileTableStorageByTableId);
    }
}
