package dev.jdata.db.utils.adt.numbers.integers;

public interface ICachedMutableLargeIntegerAllocator extends IMutableLargeIntegerAllocator<ICachedMutableLargeInteger> {

    public static ICachedMutableLargeIntegerAllocator create() {

        return new CachedMutableLargeIntegerAllocator();
    }
}
