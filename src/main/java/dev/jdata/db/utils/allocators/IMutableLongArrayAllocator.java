package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.arrays.MutableLongArray;

public interface IMutableLongArrayAllocator {

    MutableLongArray allocateLongArray(int minimumCapacity);

    void freeLongArray(MutableLongArray longArray);
}
