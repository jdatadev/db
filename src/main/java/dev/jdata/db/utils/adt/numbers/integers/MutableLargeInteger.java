package dev.jdata.db.utils.adt.numbers.integers;

import java.util.Objects;

import dev.jdata.db.utils.adt.numbers.decimals.IDecimalView;

abstract class MutableLargeInteger<T extends MutableLargeInteger<T>> extends BaseLargeInteger<T> implements IMutableLargeInteger {

    MutableLargeInteger(AllocationType allocationType, int precision) {
        super(allocationType, precision);
    }

    MutableLargeInteger(AllocationType allocationType, ILargeIntegerView largeInteger) {
        super(allocationType, largeInteger);
    }

    @Override
    public final void clear() {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void setValue(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

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
    public final void add(ILargeIntegerView largeInteger) {

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

    @Override
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
