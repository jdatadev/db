package dev.jdata.db.utils.adt.numbers.integers;

public interface IMutableLargeIntegerAllocator<T extends IMutableLargeInteger> {

    @Deprecated // precision?
    T allocateMutableLargeInteger(int precision);
    T allocateMutableLargeInteger(ILargeIntegerView largeInteger);

    void freeMutableLargeInteger(T largeInteger);
}
