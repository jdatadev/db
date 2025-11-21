package dev.jdata.db.utils.adt.numbers.integers;

import dev.jdata.db.utils.adt.numbers.BaseNumber;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLargeInteger<T extends BaseLargeInteger<T>> extends BaseNumber<T> implements ILargeIntegerView {

    BaseLargeInteger(AllocationType allocationType, int precision) {
        super(allocationType, precision);

        Checks.isIntegerPrecision(precision);
    }

    BaseLargeInteger(AllocationType allocationType, ILargeIntegerView largeInteger) {
        super(allocationType, largeInteger);
    }

    @Override
    public final boolean isGreaterThan(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isLessThan(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isGreaterThanOrEqualTo(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isLessThanOrEqualTo(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final long longValueExact() {

        throw new UnsupportedOperationException();
    }
}
