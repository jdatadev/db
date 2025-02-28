package dev.jdata.db.sql.ast;

import dev.jdata.db.sql.parse.ParserAllocator;
import dev.jdata.db.sql.parse.expression.LargeInteger;
import dev.jdata.db.utils.adt.decimals.Decimal;

public interface SQLAllocator extends ParserAllocator {

    Decimal allocateDecimal(long beforeDecimalPoint, long afterDecimalPoint);
    Decimal allocateDecimal(LargeInteger beforeDecimalPoint, long afterDecimalPoint);
    Decimal allocateDecimal(long beforeDecimalPoint, LargeInteger afterDecimalPoint);
    Decimal allocateDecimal(LargeInteger beforeDecimalPoint, LargeInteger afterDecimalPoint);

    void freeDecimal(Decimal decimal);

    void freeLargeInteger(LargeInteger largeInteger);
}
