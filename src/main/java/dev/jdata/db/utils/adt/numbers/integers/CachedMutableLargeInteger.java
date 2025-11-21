package dev.jdata.db.utils.adt.numbers.integers;

final class CachedMutableLargeInteger extends MutableLargeInteger<CachedMutableLargeInteger> implements ICachedMutableLargeInteger {

    static CachedMutableLargeInteger create(AllocationType allocationType, int precision) {

        AllocationType.checkIsCached(allocationType);

        return new CachedMutableLargeInteger(allocationType, precision);
    }

    private CachedMutableLargeInteger(AllocationType allocationType, int precision) {
        super(allocationType, precision);
    }
}
