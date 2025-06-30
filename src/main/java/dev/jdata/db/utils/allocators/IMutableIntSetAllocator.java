package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.sets.IMutableIntSet;

public interface IMutableIntSetAllocator<T extends IMutableIntSet> extends IInstanceAllocator<T> {

    T allocateMutableIntSet(int minimumCapacityExponent);

    void freeMutableIntSet(T intSet);
}
