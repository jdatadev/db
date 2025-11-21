package dev.jdata.db.utils.adt.lists;

public interface ICachedLongIndexListAllocator extends ILongIndexListAllocator<ICachedLongIndexList, ICachedMutableLongIndexList, ICachedLongIndexListBuilder> {

    public static ICachedLongIndexListAllocator create() {

        return new CachedLongIndexListAllocator(long[]::new, new CachedMutableLongIndexListAllocator());
    }
}
