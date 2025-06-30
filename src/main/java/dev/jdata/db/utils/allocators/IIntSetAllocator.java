package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.sets.IIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;

public interface IIntSetAllocator<T extends IIntSet> extends IInstanceAllocator<T> {

    T allocateIntSet(int minimumCapacityExponent);

    void freeIntSet(T intSet);

    T copyToImmutable(IMutableIntSet intSet);
}
