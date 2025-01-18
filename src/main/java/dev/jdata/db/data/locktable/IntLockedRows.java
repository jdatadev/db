package dev.jdata.db.data.locktable;

import java.util.Objects;

import dev.jdata.db.data.BaseRowMap;
import dev.jdata.db.data.locktable.LockTable.LockType;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

final class IntLockedRows extends LockedRows {

    private final long[] lockedRows;
    private final long[] values;

    IntLockedRows(long[] lockedRows, long[] values) {

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

        return BaseRowMap.getTableId(lockedRows[intIndex(index)]);
    }

    @Override
    public long getRowId(long index) {

        return BaseRowMap.getRowId(lockedRows[intIndex(index)]);
    }

    @Override
    public int getNumLocks(long index, LockType lockType) {

        return getNumLocksValue(values[intIndex(index)], lockType);
    }

    private static int intIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index);
    }
}
