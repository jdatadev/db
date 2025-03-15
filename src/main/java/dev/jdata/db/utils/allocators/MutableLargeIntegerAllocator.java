package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.integers.MutableLargeInteger;

public final class MutableLargeIntegerAllocator extends BaseArrayAllocator<MutableLargeInteger> implements ILargeIntegerAllocator {

    public MutableLargeIntegerAllocator() {
        super(MutableLargeInteger::ofPrecision, MutableLargeInteger::getPrecision);
    }

    @Override
    public MutableLargeInteger allocateLargeInteger(int precision) {

        return MutableLargeInteger.ofPrecision(precision);
    }

    @Override
    public void freeLargeInteger(MutableLargeInteger largeInteger) {

        freeArrayInstance(largeInteger);
    }
}
