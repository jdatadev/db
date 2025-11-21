package dev.jdata.db.data.locktable;

import dev.jdata.db.LockType;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

abstract class LockedTableElements implements IOnlyElementsView {

    public abstract int getNumLocks(long index, LockType lockType);
}
