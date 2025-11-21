package dev.jdata.db.utils.adt.numbers.decimals;

import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;

abstract class ImmutableDecimal<T extends ImmutableDecimal<T>> extends BaseDecimal<T> implements IDecimal {

    ImmutableDecimal(AllocationType allocationType, int precision, int scale) {
        super(allocationType, precision, scale);
    }

    @Override
    public final IDecimal setValue(IDecimalView decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal setValue(ILargeIntegerView largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal setValue(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal setValue(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal add(IDecimalView decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal add(ILargeIntegerView largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal add(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal add(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal subtract(IDecimalView decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal subtract(ILargeIntegerView largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal subtract(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal subtract(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal multiply(IDecimalView decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal multiply(ILargeIntegerView largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal multiply(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal multiply(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal divide(IDecimalView decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal divide(ILargeIntegerView largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal divide(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal divide(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal modulus(IDecimalView decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal modulus(ILargeIntegerView largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal modulus(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final IDecimal modulus(double d) {

        throw new UnsupportedOperationException();
    }
}
