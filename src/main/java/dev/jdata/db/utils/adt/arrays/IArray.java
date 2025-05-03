package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.Contains;
import dev.jdata.db.utils.adt.byindex.IByIndex;
import dev.jdata.db.utils.adt.elements.ICapacity;

public interface IArray extends Contains, ICapacity, IByIndex {

    long getLimit();
}
