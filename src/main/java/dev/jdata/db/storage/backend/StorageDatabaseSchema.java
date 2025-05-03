package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.data.tables.TableByIdMap;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CheckedExceptionFunction;

public final class StorageDatabaseSchema {

    private final TableByIdMap<StorageTableFileSchema> storageTableSchemaByTableId;

    <E extends Exception> StorageDatabaseSchema(IEffectiveDatabaseSchema databaseSchema, CheckedExceptionFunction<Table, StorageTableFileSchema, E> storageTableSchemaFactory)
            throws E {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(storageTableSchemaFactory);

        this.storageTableSchemaByTableId = new TableByIdMap<>(databaseSchema, StorageTableFileSchema[]::new, storageTableSchemaFactory);
    }

    StorageTableFileSchema getStorageTableSchema(int tableId) {

        Checks.isTableId(tableId);

        return storageTableSchemaByTableId.getTable(tableId);
    }
}
