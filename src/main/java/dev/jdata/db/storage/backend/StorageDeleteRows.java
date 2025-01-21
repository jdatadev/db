package dev.jdata.db.storage.backend;

import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.storage.backend.StorageDMLRows.StorageDMLRow;

public final class StorageDeleteRows extends StorageDMLRows<StorageDeleteRows.DeleteRow> {

    static final class DeleteRow extends StorageDMLRow {

        void initialize(DatabaseSchemaVersion databaseSchemaVersion, long rowId, long transactionId) {

            initializeDMLRow(databaseSchemaVersion, rowId, transactionId);
        }
    }
}
