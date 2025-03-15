package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.adt.elements.IElements;

public interface ILongArrayGetters extends IElements, ICapacity {

    long get(long index);
}
