package dev.jdata.db.utils.adt.numbers.decimals;

final class CachedMutableDecimal extends MutableDecimal<CachedMutableDecimal> implements ICachedMutableDecimal {

    CachedMutableDecimal(AllocationType allocationType, int precision) {
        super(allocationType, precision);

        AllocationType.checkIsCached(allocationType);
    }
}
