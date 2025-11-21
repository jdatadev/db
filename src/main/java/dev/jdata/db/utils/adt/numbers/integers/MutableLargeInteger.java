package dev.jdata.db.utils.adt.numbers.integers;

import java.util.Objects;

import dev.jdata.db.utils.adt.numbers.decimals.IDecimalView;

abstract class MutableLargeInteger<T extends MutableLargeInteger<T>> extends BaseLargeInteger<T> implements IMutableLargeInteger {

    MutableLargeInteger(AllocationType allocationType, int precision) {
        super(allocationType, precision);
    }

    @Override
    public final int compareTo(T other) {

        Objects.requireNonNull(other);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void setValue(ILargeIntegerCommon largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
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

    @Override
    public final void setValue(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void setValue(CharSequence integerCharSequence) {

        Objects.requireNonNull(integerCharSequence);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(ILargeIntegerCommon largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void subtract(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void subtract(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void multiply(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    public final void multiply(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean divideIfModulusZero(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean divideIfModulusZero(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void modulus(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void modulus(IDecimalView decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void modulus(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void modulus(double d) {

        throw new UnsupportedOperationException();
    }
}
