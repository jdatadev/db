package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.decimals.MutableDecimal;
import dev.jdata.db.utils.adt.integers.ILargeInteger;

public final class MutableDecimalAllocator extends BaseArrayAllocator<MutableDecimal> implements IMutableDecimalAllocator {

    public MutableDecimalAllocator() {
        super(MutableDecimal::ofPrecision, MutableDecimal::getPrecision);
    }

    @Override
    public MutableDecimal allocateDecimal(long beforeDecimalPoint, long afterDecimalPoint) {

        throw new UnsupportedOperationException();
    }

    @Override
    public MutableDecimal allocateDecimal(ILargeInteger beforeDecimalPoint, long afterDecimalPoint) {

        throw new UnsupportedOperationException();
    }

    @Override
    public MutableDecimal allocateDecimal(long beforeDecimalPoint, ILargeInteger afterDecimalPoint) {

        throw new UnsupportedOperationException();
    }

    @Override
    public MutableDecimal allocateDecimal(ILargeInteger beforeDecimalPoint, ILargeInteger afterDecimalPoint) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void freeDecimal(MutableDecimal decimal) {

        freeArrayInstance(decimal);
    }
}
