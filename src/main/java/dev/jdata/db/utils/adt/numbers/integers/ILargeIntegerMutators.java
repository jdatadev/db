package dev.jdata.db.utils.adt.numbers.integers;

import dev.jdata.db.utils.adt.numbers.INumberMutatorsMarker;
import dev.jdata.db.utils.adt.numbers.decimals.IDecimalView;

interface ILargeIntegerMutators extends INumberMutatorsMarker {

    void setValue(ILargeIntegerView largeInteger);
    void setValue(long integer);
    void setValue(CharSequence integerCharSequence);

    void add(ILargeIntegerView largeInteger);
    void add(long integer);

    void subtract(ILargeIntegerView largeInteger);
    void subtract(long integer);

    void multiply(ILargeIntegerView largeInteger);
    void multiply(long integer);

    boolean divideIfModulusZero(ILargeIntegerView largeInteger);
    boolean divideIfModulusZero(long l);

    void modulus(ILargeIntegerView largeInteger);
    void modulus(IDecimalView decimal);
    void modulus(long l);
    @Deprecated
    void modulus(double d);
}
