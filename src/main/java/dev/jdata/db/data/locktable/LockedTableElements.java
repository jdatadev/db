package dev.jdata.db.data.locktable;

import dev.jdata.db.LockType;
import dev.jdata.db.utils.adt.elements.Elements;

abstract class LockedTableElements implements Elements {

    public abstract int getNumLocks(long index, LockType lockType);
}
