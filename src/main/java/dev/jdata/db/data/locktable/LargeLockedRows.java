package dev.jdata.db.data.locktable;

import java.util.Objects;

import dev.jdata.db.LockType;
import dev.jdata.db.data.BaseRowMap;
import dev.jdata.db.utils.adt.arrays.LongLargeArray;
import dev.jdata.db.utils.checks.Checks;

final class LargeLockedRows extends LockedRows {

    private final LongLargeArray lockedRows;
    private final LongLargeArray values;

    LargeLockedRows(LongLargeArray lockedRows, LongLargeArray values) {

        Objects.requireNonNull(lockedRows);
        Objects.requireNonNull(values);
        Checks.areSameNumElements(lockedRows, values);

        this.lockedRows = lockedRows;
        this.values = values;
    }

    @Override
    public boolean isEmpty() {

        return lockedRows.isEmpty();
    }

    @Override
    public long getNumElements() {

        return lockedRows.getNumElements();
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
