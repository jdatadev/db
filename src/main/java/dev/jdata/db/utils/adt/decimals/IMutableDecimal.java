package dev.jdata.db.utils.adt.decimals;

import dev.jdata.db.utils.adt.integers.ILargeInteger;

public interface IMutableDecimal extends IDecimal {

    void setValue(IDecimal decimal);
    void setValue(ILargeInteger largeInteger);
    void setValue(long integer);
    @Deprecated
    void setValue(double d);

    void add(IDecimal decimal);
    void add(ILargeInteger largeInteger);
    void add(long l);
    @Deprecated
    void add(double d);

    void subtract(IDecimal decimal);
    void subtract(ILargeInteger largeInteger);
    void subtract(long l);
    @Deprecated
    void subtract(double d);

    void multiply(IDecimal decimal);
    void multiply(ILargeInteger largeInteger);
    void multiply(long l);
    @Deprecated
    void multiply(double d);

    void divide(IDecimal decimal);
    void divide(ILargeInteger largeInteger);
    void divide(long l);
    @Deprecated
    void divide(double d);

    void modulus(IDecimal decimal);
    void modulus(ILargeInteger largeInteger);
    void modulus(long l);
    void modulus(double d);
}
