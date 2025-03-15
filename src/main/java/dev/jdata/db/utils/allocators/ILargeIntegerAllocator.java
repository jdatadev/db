package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.integers.MutableLargeInteger;

public interface ILargeIntegerAllocator {

    @Deprecated // precision?
    MutableLargeInteger allocateLargeInteger(int precision);

    void freeLargeInteger(MutableLargeInteger largeInteger);
}
