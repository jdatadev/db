package dev.jdata.db.utils.adt.numbers.decimals;

import dev.jdata.db.utils.adt.numbers.ILargeNumber;
import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;

public interface IDecimal extends ILargeNumber, IDecimalCommon {

    IDecimal setValue(IDecimalView decimal);
    IDecimal setValue(ILargeIntegerView largeInteger);
    IDecimal setValue(long integer);
    @Deprecated
    IDecimal setValue(double d);

    IDecimal add(IDecimalView decimal);
    IDecimal add(ILargeIntegerView largeInteger);
    IDecimal add(long l);
    @Deprecated
    IDecimal add(double d);

    IDecimal subtract(IDecimalView decimal);
    IDecimal subtract(ILargeIntegerView largeInteger);
    IDecimal subtract(long l);
    @Deprecated
    IDecimal subtract(double d);

    IDecimal multiply(IDecimalView decimal);
    IDecimal multiply(ILargeIntegerView largeInteger);
    IDecimal multiply(long l);
    @Deprecated
    IDecimal multiply(double d);

    IDecimal divide(IDecimalView decimal);
    IDecimal divide(ILargeIntegerView largeInteger);
    IDecimal divide(long l);
    @Deprecated
    IDecimal divide(double d);

    IDecimal modulus(IDecimalView decimal);
    IDecimal modulus(ILargeIntegerView largeInteger);
    IDecimal modulus(long l);
    IDecimal modulus(double d);
}
