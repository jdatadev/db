package dev.jdata.db.dml;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.Elements;
import dev.jdata.db.utils.checks.Checks;

public abstract class StorageRows<T> implements Elements {

    private T rows;
    private int numRows;

    protected void initialize(T rows, int numRows) {

        this.rows = Objects.requireNonNull(rows);
        this.numRows = Checks.isNumElements(numRows);
    }

    @Override
    public final boolean isEmpty() {

        return numRows == 0;
    }

    @Override
    public final long getNumElements() {
        return numRows;
    }

    @Deprecated
    public final int getNumRows() {
        return numRows;
    }

    protected final T getRows() {
        return rows;
    }
}
