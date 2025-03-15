package dev.jdata.db.utils.adt.integers;

import dev.jdata.db.utils.adt.decimals.IDecimal;

public interface IMutableLargeInteger extends ILargeInteger {

    void setValue(ILargeInteger largeInteger);
    void setValue(long integer);
    void setValue(CharSequence integerCharSequence);

    void add(ILargeInteger largeInteger);
    void add(long integer);

    void subtract(ILargeInteger largeInteger);
    void subtract(long integer);

    void multiply(ILargeInteger largeInteger);

    boolean divideIfModulusZero(ILargeInteger largeInteger);
    boolean divideIfModulusZero(long l);

    void modulus(ILargeInteger largeInteger);
    void modulus(IDecimal decimal);
    void modulus(long l);
    @Deprecated
    void modulus(double d);
}
