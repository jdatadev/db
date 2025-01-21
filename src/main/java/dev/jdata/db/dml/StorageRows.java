package dev.jdata.db.dml;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public abstract class StorageRows<T> {

    private T rows;
    private int numRows;

    protected final void init(T rows, int numRows) {

        this.rows = Objects.requireNonNull(rows);
        this.numRows = Checks.isNumElements(numRows);
    }

    protected final T getRows() {
        return rows;
    }

    public final int getNumRows() {
        return numRows;
    }
}
