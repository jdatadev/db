package dev.jdata.db.storage.backend;

import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.storage.backend.DMLRows.DMLRow;

public final class DeleteRows extends DMLRows<DeleteRows.DeleteRow> {

    static final class DeleteRow extends DMLRow {

        void initialize(DatabaseSchemaVersion databaseSchemaVersion, long rowId, long transactionId) {

            initializeDMLRow(databaseSchemaVersion, rowId, transactionId);
        }
    }
}
