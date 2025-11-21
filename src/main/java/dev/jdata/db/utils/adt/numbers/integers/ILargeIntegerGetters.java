package dev.jdata.db.utils.adt.numbers.integers;

import dev.jdata.db.utils.adt.numbers.INumberGettersMarker;

interface ILargeIntegerGetters extends INumberGettersMarker {

    boolean isGreaterThan(long integer);
    boolean isLessThan(long integer);
    boolean isGreaterThanOrEqualTo(long integer);
    boolean isLessThanOrEqualTo(long integer);

    long longValueExact();
}
