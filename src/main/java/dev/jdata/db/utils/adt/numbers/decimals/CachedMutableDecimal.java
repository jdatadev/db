package dev.jdata.db.utils.adt.numbers.decimals;

final class CachedMutableDecimal extends MutableDecimal<CachedMutableDecimal> implements ICachedMutableDecimal {

    static CachedMutableDecimal create(AllocationType allocationType, int precision) {

        AllocationType.checkIsCached(allocationType);

        return new CachedMutableDecimal(allocationType, precision);
    }

    private CachedMutableDecimal(AllocationType allocationType, int precision) {
        super(allocationType, precision);
    }
}
