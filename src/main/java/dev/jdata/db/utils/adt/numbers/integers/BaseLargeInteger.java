package dev.jdata.db.utils.adt.numbers.integers;

import dev.jdata.db.utils.adt.numbers.BaseNumber;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLargeInteger<T extends BaseLargeInteger<T>> extends BaseNumber<T> implements ILargeIntegerView {

    private int precision;

    BaseLargeInteger(AllocationType allocationType, int precision) {
        super(allocationType);

        Checks.isIntegerPrecision(precision);

        this.precision = Checks.isIntegerPrecision(precision);
    }

    @Override
    public final int getPrecision() {

        return precision;
    }
}
