package dev.jdata.db.utils.adt.numbers.decimals;

import dev.jdata.db.utils.adt.numbers.INumberMutatorsMarker;
import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;

interface IDecimalMutators extends INumberMutatorsMarker {

    void setValue(IDecimalView decimal);
    void setValue(ILargeIntegerView largeInteger);
    void setValue(long integer);
    @Deprecated
    void setValue(double d);

    void add(IDecimalView decimal);
    void add(ILargeIntegerView largeInteger);
    void add(long l);
    @Deprecated
    void add(double d);

    void subtract(IDecimalView decimal);
    void subtract(ILargeIntegerView largeInteger);
    void subtract(long l);
    @Deprecated
    void subtract(double d);

    void multiply(IDecimalView decimal);
    void multiply(ILargeIntegerView largeInteger);
    void multiply(long l);
    @Deprecated
    void multiply(double d);

    void divide(IDecimalView decimal);
    void divide(ILargeIntegerView largeInteger);
    void divide(long l);
    @Deprecated
    void divide(double d);

    void modulus(IDecimalView decimal);
    void modulus(ILargeIntegerView largeInteger);
    void modulus(long l);
    void modulus(double d);
}
