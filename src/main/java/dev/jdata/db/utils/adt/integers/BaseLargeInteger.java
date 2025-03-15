package dev.jdata.db.utils.adt.integers;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseLargeInteger<T extends BaseLargeInteger<T>> implements ILargeInteger, Comparable<T> {

    private int precision;

    BaseLargeInteger(int precision, boolean disamiguate) {

        Checks.isIntegerPrecision(precision);

        this.precision = Checks.isIntegerPrecision(precision);
    }

    @Override
    public final int getPrecision() {

        return precision;
    }
}
