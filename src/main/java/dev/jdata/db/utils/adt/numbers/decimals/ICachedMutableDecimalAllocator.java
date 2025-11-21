package dev.jdata.db.utils.adt.numbers.decimals;

public interface ICachedMutableDecimalAllocator extends IMutableDecimalAllocator<ICachedMutableDecimal> {

    public static ICachedMutableDecimalAllocator create() {

        return new CachedMutableDecimalAllocator();
    }
}
