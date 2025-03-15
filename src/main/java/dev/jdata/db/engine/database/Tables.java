package dev.jdata.db.engine.database;

import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.utils.checks.Checks;

public final class Tables {

    private TableState[] tables;

    Tables(DatabaseSchema databaseSchema, long[] initialRowIds) {

        Objects.requireNonNull(databaseSchema);

        final int numTables = databaseSchema.getMaxTableId() + 1;

        if (initialRowIds != null) {

            Checks.areEqual(numTables, initialRowIds.length);
        }

        this.tables = new TableState[numTables];

        for (int tableId = 0; tableId < numTables; ++ tableId) {

            tables[tableId] = new TableState(initialRowIds != null ?initialRowIds[tableId] : DBConstants.INITIAL_ROW_ID);
        }
    }

    long allocateRowId(int tableId) {

        Checks.isTableId(tableId);

        return tables[tableId].allocateRowId();
    }
}
