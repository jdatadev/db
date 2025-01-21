package dev.jdata.db.engine.database;

import dev.jdata.db.utils.checks.Checks;

final class TableState {

    private long rowIdAllocator;

    TableState(long initialRowId) {

        this.rowIdAllocator = Checks.isRowId(initialRowId);
    }

    synchronized long allocateRowId() {

        return rowIdAllocator ++;
    }
}
