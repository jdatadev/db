package dev.jdata.db.utils.adt.integers;

import java.util.Objects;

import dev.jdata.db.utils.adt.decimals.IDecimal;
import dev.jdata.db.utils.checks.Checks;

public final class MutableLargeInteger extends BaseLargeInteger<MutableLargeInteger> implements IMutableLargeInteger {

    public static MutableLargeInteger newInstance() {

        return ofPrecision(16);
    }

    public static MutableLargeInteger ofPrecision(int precision) {

        Checks.isIntegerPrecision(precision);

        return new MutableLargeInteger(precision, false);
    }

    private MutableLargeInteger(int precision, boolean disamiguate) {
        super(precision, disamiguate);
    }

    @Override
    public int compareTo(MutableLargeInteger o) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGreaterThan(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLessThan(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGreaterThanOrEqualTo(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLessThanOrEqualTo(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public long longValueExact() {

        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(CharSequence integerCharSequence) {

        Objects.requireNonNull(integerCharSequence);

        throw new UnsupportedOperationException();
    }

    @Override
    public void add(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void add(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void subtract(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void subtract(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void multiply(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    public void multiply(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean divideIfModulusZero(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean divideIfModulusZero(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void modulus(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void modulus(IDecimal decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public void modulus(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void modulus(double d) {

        throw new UnsupportedOperationException();
    }
}
