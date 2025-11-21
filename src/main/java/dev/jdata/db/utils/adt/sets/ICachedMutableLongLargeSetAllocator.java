package dev.jdata.db.utils.adt.sets;

public interface ICachedMutableLongLargeSetAllocator extends IMutableLongLargeSetAllocator<ICachedMutableLongLargeSet> {

    public static ICachedMutableLongLargeSetAllocator create() {

        return new CachedMutableLongLargeSetAllocator();
    }
}
