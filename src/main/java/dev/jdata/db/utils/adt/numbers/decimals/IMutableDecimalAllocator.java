package dev.jdata.db.utils.adt.numbers.decimals;

import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;

public interface IMutableDecimalAllocator<T extends IMutableDecimal> {

    T allocateDecimal(long beforeDecimalPoint, long afterDecimalPoint);
    T allocateDecimal(ILargeIntegerView beforeDecimalPoint, long afterDecimalPoint);
    T allocateDecimal(long beforeDecimalPoint, ILargeIntegerView afterDecimalPoint);
    T allocateDecimal(ILargeIntegerView beforeDecimalPoint, ILargeIntegerView afterDecimalPoint);

    void freeDecimal(T decimal);
}
