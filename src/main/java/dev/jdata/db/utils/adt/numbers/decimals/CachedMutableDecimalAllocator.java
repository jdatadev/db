package dev.jdata.db.utils.adt.numbers.decimals;

import dev.jdata.db.utils.adt.numbers.CachedMutableLargeNumberAllocator;
import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;

final class CachedMutableDecimalAllocator extends CachedMutableLargeNumberAllocator<CachedMutableDecimal> implements ICachedMutableDecimalAllocator {

    CachedMutableDecimalAllocator() {
        super(CachedMutableDecimal::new);
    }

    @Override
    public ICachedMutableDecimal allocateDecimal(long beforeDecimalPoint, long afterDecimalPoint) {

        throw new UnsupportedOperationException();
    }

    @Override
    public ICachedMutableDecimal allocateDecimal(ILargeIntegerView beforeDecimalPoint, long afterDecimalPoint) {

        throw new UnsupportedOperationException();
    }

    @Override
    public ICachedMutableDecimal allocateDecimal(long beforeDecimalPoint, ILargeIntegerView afterDecimalPoint) {

        throw new UnsupportedOperationException();
    }

    @Override
    public ICachedMutableDecimal allocateDecimal(ILargeIntegerView beforeDecimalPoint, ILargeIntegerView afterDecimalPoint) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void freeDecimal(ICachedMutableDecimal decimal) {

        freeArrayInstance((CachedMutableDecimal)decimal);
    }
}
