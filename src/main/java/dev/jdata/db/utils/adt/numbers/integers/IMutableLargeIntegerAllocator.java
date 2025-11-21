package dev.jdata.db.utils.adt.numbers.integers;

public interface IMutableLargeIntegerAllocator<T extends IMutableLargeInteger> {

    @Deprecated // precision?
    T allocateLargeInteger(int precision);
    T allocateLargeInteger(ILargeIntegerView largeInteger);

    void freeLargeInteger(T largeInteger);
}
