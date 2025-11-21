package dev.jdata.db.utils.adt.numbers.integers;

final class CachedMutableLargeInteger extends MutableLargeInteger<CachedMutableLargeInteger> implements ICachedMutableLargeInteger {

    CachedMutableLargeInteger(AllocationType allocationType, int precision) {
        super(allocationType, precision);

        AllocationType.checkIsCached(allocationType);
    }
}
