package dev.jdata.db.engine.sessions;

import dev.jdata.db.utils.adt.arrays.LargeLongArray;

public interface ILargeLongArrayAllocator {

    LargeLongArray allocateLargeLongArray();

    void freeLargeLongArray(LargeLongArray largeLongArray);
}
