package dev.jdata.db.utils.adt.integers;

import dev.jdata.db.utils.adt.numbers.ILargeNumber;

public interface ILargeInteger extends ILargeNumber {

    boolean isGreaterThan(long integer);
    boolean isLessThan(long integer);
    boolean isGreaterThanOrEqualTo(long integer);
    boolean isLessThanOrEqualTo(long integer);

    long longValueExact();
}
