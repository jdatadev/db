package dev.jdata.db.engine.database;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.utils.checks.Checks;

public final class Tables {

    private TableState[] tables;

    Tables(DatabaseSchema databaseSchema, long[] initialRowIds) {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(initialRowIds);

        final int numTables = databaseSchema.getMaxTableId() + 1;

        Checks.areEqual(numTables, initialRowIds.length);

        this.tables = new TableState[numTables];

        for (int tableId = 0; tableId < numTables; ++ tableId) {

            tables[tableId] = new TableState(initialRowIds[tableId]);
        }
    }

    long allocateRowId(int tableId) {

        Checks.isTableId(tableId);

        return tables[tableId].allocateRowId();
    }
}
