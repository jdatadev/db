package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public final class ReadRows {

    private long[] rowIds;
    private int offset;
    private int numRowIds;

    void init(long[] rowIds, int offset, int numRowIds) {

        this.rowIds = Objects.requireNonNull(rowIds);
        this.offset = Checks.isOffset(offset);
        this.numRowIds = Checks.isNumElements(numRowIds);
    }

    public long getRowId(int index) {

        return rowIds[index];
    }

    public int getOffset() {
        return offset;
    }

    public int getNumRowIds() {
        return numRowIds;
    }
}
