package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.arrays.LongArray;

public interface ILongArrayAllocator {

    LongArray allocateLongArray(int minimumCapacity);

    void freeLongArray(LongArray longArray);
}
