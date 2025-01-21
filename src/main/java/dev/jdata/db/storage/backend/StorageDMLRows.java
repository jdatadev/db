package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.dml.DMLRows;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.utils.checks.Checks;

public abstract class StorageDMLRows<T extends StorageDMLRows.StorageDMLRow> extends DMLRows<T> {

    static abstract class StorageDMLRow extends DMLRow {

        private DatabaseSchemaVersion databaseSchemaVersion;
        private long rowId;
        private long transactionId;

        final void initializeDMLRow(DatabaseSchemaVersion databaseSchemaVersion, long rowId, long transactionId) {

            this.databaseSchemaVersion = Objects.requireNonNull(databaseSchemaVersion);
            this.rowId = Checks.isRowId(rowId);
            this.transactionId = Checks.isTransactionId(transactionId);
        }

        final DatabaseSchemaVersion getDatabaseSchemaVersion() {
            return databaseSchemaVersion;
        }

        final long getRowId() {
            return rowId;
        }

        final long getTransactionId() {
            return transactionId;
        }
    }

    public final DatabaseSchemaVersion getDatabaseSchemaVersion(int index) {

        return getRows()[index].getDatabaseSchemaVersion();
    }

    public final long getRowId(int index) {

        return getRows()[index].getRowId();
    }

    public final long getTransactionId(int index) {

        return getRows()[index].getTransactionId();
    }
}
