package dev.jdata.db.data.locktable;

import java.util.Objects;

import dev.jdata.db.LockType;
import dev.jdata.db.data.BaseRowMap;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.checks.Checks;

final class IntCapacityLockedRows extends LockedRows {

    private final long[] lockedRows;
    private final long[] values;

    IntCapacityLockedRows(long[] lockedRows, long[] values) {

        Objects.requireNonNull(lockedRows);
        Objects.requireNonNull(values);
        Checks.areEqual(lockedRows.length, values.length);

        this.lockedRows = lockedRows;
        this.values = values;
    }

    @Override
    public boolean isEmpty() {

        return lockedRows.length == 0;
    }

    @Override
    public long getNumElements() {

        return lockedRows.length;
    }

    @Override
    public int getTableId(long index) {

        return BaseRowMap.getHashTableId(lockedRows[IByIndexView.intIndex(index)]);
    }

    @Override
    public long getRowId(long index) {

        return BaseRowMap.getHashRowId(lockedRows[IByIndexView.intIndex(index)]);
    }

    @Override
    public int getNumLocks(long index, LockType lockType) {

        return getNumLocksValue(values[IByIndexView.intIndex(index)], lockType);
    }
}
