package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.decimals.MutableDecimal;
import dev.jdata.db.utils.adt.integers.ILargeInteger;

public interface IMutableDecimalAllocator {

    MutableDecimal allocateDecimal(long beforeDecimalPoint, long afterDecimalPoint);
    MutableDecimal allocateDecimal(ILargeInteger beforeDecimalPoint, long afterDecimalPoint);
    MutableDecimal allocateDecimal(long beforeDecimalPoint, ILargeInteger afterDecimalPoint);
    MutableDecimal allocateDecimal(ILargeInteger beforeDecimalPoint, ILargeInteger afterDecimalPoint);

    void freeDecimal(MutableDecimal decimal);
}
