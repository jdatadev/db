package dev.jdata.db.utils.adt.decimals;

import dev.jdata.db.utils.adt.integers.ILargeInteger;

public interface IImmutableDecimal extends IDecimal {

    IImmutableDecimal setValue(IDecimal decimal);
    IImmutableDecimal setValue(ILargeInteger largeInteger);
    IImmutableDecimal setValue(long integer);
    @Deprecated
    IImmutableDecimal setValue(double d);

    IImmutableDecimal add(IDecimal decimal);
    IImmutableDecimal add(ILargeInteger largeInteger);
    IImmutableDecimal add(long l);
    @Deprecated
    IImmutableDecimal add(double d);

    IImmutableDecimal subtract(IDecimal decimal);
    IImmutableDecimal subtract(ILargeInteger largeInteger);
    IImmutableDecimal subtract(long l);
    @Deprecated
    IImmutableDecimal subtract(double d);

    IImmutableDecimal multiply(IDecimal decimal);
    IImmutableDecimal multiply(ILargeInteger largeInteger);
    IImmutableDecimal multiply(long l);
    @Deprecated
    IImmutableDecimal multiply(double d);

    IImmutableDecimal divide(IDecimal decimal);
    IImmutableDecimal divide(ILargeInteger largeInteger);
    IImmutableDecimal divide(long l);
    @Deprecated
    IImmutableDecimal divide(double d);

    IImmutableDecimal modulus(IDecimal decimal);
    IImmutableDecimal modulus(ILargeInteger largeInteger);
    IImmutableDecimal modulus(long l);
    IImmutableDecimal modulus(double d);
}
