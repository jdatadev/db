package dev.jdata.db.data.locktable;

import dev.jdata.db.data.locktable.LockTable.LockType;
import dev.jdata.db.utils.adt.elements.Elements;
import dev.jdata.db.utils.scalars.Integers;

abstract class LockedRows implements Elements {

    public abstract int getTableId(long index);
    public abstract long getRowId(long index);
    public abstract int getNumLocks(long index, LockType lockType);

    final int getNumLocksValue(long lockBits, LockType lockType) {

        return Integers.checkUnsignedLongToUnsignedInt((lockBits & lockType.mask()) >>> lockType.shift());
    }
}
