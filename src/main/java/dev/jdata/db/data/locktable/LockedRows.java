package dev.jdata.db.data.locktable;

import dev.jdata.db.LockType;
import dev.jdata.db.utils.scalars.Integers;

abstract class LockedRows extends LockedTableElements {

    public abstract int getTableId(long index);
    public abstract long getRowId(long index);

    final int getNumLocksValue(long lockBits, LockType lockType) {

        return Integers.checkUnsignedLongToUnsignedInt((lockBits & LockTable.mask(lockType)) >>> LockTable.shift(lockType));
    }
}
