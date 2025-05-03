package dev.jdata.db.data.locktable;

import java.util.Objects;

import dev.jdata.db.LockType;
import dev.jdata.db.data.BaseRowMap;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.checks.Checks;

final class LargeLockedRows extends LockedRows {

    private final LargeLongArray lockedRows;
    private final LargeLongArray values;

    LargeLockedRows(LargeLongArray lockedRows, LargeLongArray values) {

        Objects.requireNonNull(lockedRows);
        Objects.requireNonNull(values);
        Checks.isSameLimit(lockedRows, values);

        this.lockedRows = lockedRows;
        this.values = values;
    }

    @Override
    public boolean isEmpty() {

        return lockedRows.getLimit() == 0;
    }

    @Override
    public long getNumElements() {

        return lockedRows.getLimit();
    }

    @Override
    public int getTableId(long index) {

        return BaseRowMap.getHashTableId(lockedRows.get(index));
    }

    @Override
    public long getRowId(long index) {

        return BaseRowMap.getHashRowId(lockedRows.get(index));
    }

    @Override
    public int getNumLocks(long index, LockType lockType) {

        return getNumLocksValue(values.get(index), lockType);
    }
}
