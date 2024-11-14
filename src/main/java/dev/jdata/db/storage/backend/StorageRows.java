package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

abstract class StorageRows<T> {

    private T rows;
    private int numRows;

    final void init(T rows, int numRows) {

        this.rows = Objects.requireNonNull(rows);
        this.numRows = Checks.isNumElements(numRows);
    }

    final T getRows() {
        return rows;
    }

    public final int getNumRows() {
        return numRows;
    }
}
