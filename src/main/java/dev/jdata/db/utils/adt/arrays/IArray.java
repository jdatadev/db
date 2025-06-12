package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.adt.byindex.IByIndex;
import dev.jdata.db.utils.adt.elements.ICapacity;

public interface IArray extends IContains, ICapacity, IByIndex {

    long getLimit();
}
