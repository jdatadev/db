package dev.jdata.db.utils.adt.arrays;

public interface ICachedMutableLongLargeArrayAllocator extends IMutableLongLargeArrayAllocator<ICachedMutableLongLargeArray> {

    public static ICachedMutableLongLargeArrayAllocator create() {

        return new CachedMutableLongLargeArrayAllocator();
    }
}
