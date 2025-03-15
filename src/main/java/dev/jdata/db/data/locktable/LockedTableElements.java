package dev.jdata.db.data.locktable;

import dev.jdata.db.LockType;
import dev.jdata.db.utils.adt.elements.IElements;

abstract class LockedTableElements implements IElements {

    public abstract int getNumLocks(long index, LockType lockType);
}
