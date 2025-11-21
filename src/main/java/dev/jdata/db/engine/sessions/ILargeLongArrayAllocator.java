package dev.jdata.db.engine.sessions;

import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;

public interface ILargeLongArrayAllocator {

    IMutableLongLargeArray allocateLargeLongArray();

    void freeLargeLongArray(IMutableLongLargeArray largeLongArray);
}
