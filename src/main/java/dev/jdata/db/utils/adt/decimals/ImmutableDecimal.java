package dev.jdata.db.utils.adt.decimals;

import dev.jdata.db.utils.adt.integers.ILargeInteger;

public final class ImmutableDecimal extends BaseDecimal<ImmutableDecimal> implements IImmutableDecimal {

    @Override
    public int compareTo(ImmutableDecimal other) {

        return compareToOther(other);
    }

    @Override
    public IImmutableDecimal setValue(IDecimal decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal setValue(ILargeInteger largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal setValue(long integer) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal setValue(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal add(IDecimal decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal add(ILargeInteger largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal add(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal add(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal subtract(IDecimal decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal subtract(ILargeInteger largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal subtract(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal subtract(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal multiply(IDecimal decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal multiply(ILargeInteger largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal multiply(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal multiply(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal divide(IDecimal decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal divide(ILargeInteger largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal divide(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal divide(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal modulus(IDecimal decimal) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal modulus(ILargeInteger largeInteger) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal modulus(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public IImmutableDecimal modulus(double d) {

        throw new UnsupportedOperationException();
    }
}
