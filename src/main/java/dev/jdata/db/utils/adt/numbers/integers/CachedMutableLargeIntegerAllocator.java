package dev.jdata.db.utils.adt.numbers.integers;

import java.util.Objects;

import dev.jdata.db.utils.adt.numbers.CachedMutableLargeNumberAllocator;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableLargeIntegerAllocator extends CachedMutableLargeNumberAllocator<CachedMutableLargeInteger> implements ICachedMutableLargeIntegerAllocator {

    CachedMutableLargeIntegerAllocator() {
        super(CachedMutableLargeInteger::new);
    }

    @Override
    public ICachedMutableLargeInteger allocateLargeInteger(int precision) {

        Checks.isIntegerPrecision(precision);

        return allocateFromFreeListOrCreateCapacityInstance(precision);
    }

    @Override
    public ICachedMutableLargeInteger allocateLargeInteger(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        final ICachedMutableLargeInteger result = allocateFromFreeListOrCreateCapacityInstance(largeInteger.getPrecision());

        result.setValue(largeInteger);

        return result;
    }

    @Override
    public void freeLargeInteger(ICachedMutableLargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        freeArrayInstance((CachedMutableLargeInteger)largeInteger);
    }
}
